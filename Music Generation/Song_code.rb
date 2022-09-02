#
#Noah gans
#CSCI 3725
#Assingment: M8
#5/18/2021
#This program makes music! It has both a melody and a chord part to go along with it.
#It generates the melody first and generates next notes based of of the history of
#the song. The Chords are added later and match the melody.




#The funtion makes a probability matrix that will aid the selection of the
#next note. It is 32 by 8 by 4. The 32 dimension represents every possible
#note that I have selected one octave and a rest multiplied by the 4 lengths
#of notes .125, .25, .5, 1. For each note type, there is a 2d matrix that gives the
#probability of moving from the current note to any other note. It is 8 by 4
# matrix to represent the 7 pitches + rest and the 4 lengths. At the end of intilizing
#everything to 1, the total of the whole array is added to the last index of the second dimension.
#This total will be updated when any number in the 2d array is updated, so probabilites
#are easy to caculate.

def make_probability_matrix()
  #for every note and length of note we need a matrix
  #There are 8 notes each note has 4 possibilites. We need
  #32 possibilites so we need 32 arrays, probably go note
  # by note and then lengths for each so [2,3] will be an
  #E3 1/8 so full array will be of [32][9][4]
  probability_table = Array.new(32, 1) { Array.new(8, 1) {Array.new(4, 1)} }
  probability_table.each_index{|index|
    probability_table[index].push(32) #total of the score
  }
  return probability_table
end

#Make_Phrase creates the inital conditions for the program. It randomly makes notes
#which are lists size three. The first being a 1. This is the case with every
#note in this program and is an artifact of early implemetations. The next index
#holds the pitch, all of which are in octave 3. Rest is also selectable, and is in there
#twice to increase the chances of it being slected. After this a random length is
#selected. These length values will be the divided of one to get play time.
#This process is looped for the length of pattern_length which is the only input. Every
#note_list is added to a phrase list, and this is returned which represents the first
#note of the song.
def make_prase(pattern_length)
  phrase = []
  pattern_length.times do
    note_list = Array.new()
    note_list.push(1)
    note_list.push(["C3", "D3", "E3", "F3", "G3", "A3", "B3", "Rest", "Rest"].choose)
    note_list.push([1,2,4,8].choose)
    phrase.push(note_list)
  end
  return phrase
end


#This method scores a chunk of size determined by the user (intially 10).
#Two chunks are compared and scored.
#is increassed whenever the pitch/rest or length matchs in the correct order.
#Order is huge as it will lead to repition and the creation of motifs.
# The score is then returned.
def score_chunk(observed_chunk, chunk_to_match)
  score = 0
  observed_chunk.each_index{|index|
    if observed_chunk[index][1] == chunk_to_match[index][1]
      score += 1
    end
    if observed_chunk[index][2] == chunk_to_match[index][2]
      score += 1
    end
  }
  return score
end



#This method returns the first index for the proabbility matrix. It is calculated
#by multiplying the pitch index by 4 for all the lengths, and then adding the length
#index. This final index is then returned
def get_note_index(note)
  
  first = ["C3", "D3", "E3", "F3", "G3", "A3", "B3", "Rest"].find_index(note[1])
  second = [1,2,4,8].find_index(note[2])
  index = (first * 4) + second
  return index
end

#Choose_next_note and get_next_likley_note are the heart of this program. Choose_next_note
#takes a 2d probability array for a given note. The total of that array is removed and
#assigned to total. The 2d array is iterated through and if a random number generated
#at the start of the method is less than the cumulative sum of probabilities plus
#the current note probability that is the next note selected. The probability 
#of each note is cacualated by taking the value in that index and dividing it 
#by the total. The note whose probability surpased the random number is 
#selected and returned as the next note. The total is pushed back onto the 
#list for the starting note. 
def choose_next_note(note_table)
  rand_num = rand()
  running_num = 0
  total = note_table.pop + 0.0
  note_table.each_index{|i|
    note_table[i].each_index{|k|
      #if random number less than cumulative prob + current prob
      
      running_num += (note_table[i][k] / total)
      if rand_num < running_num
        pitch = ["C3", "D3", "E3", "F3", "G3", "A3", "B3", "Rest"][i]
        length = [1,2,4,8][k]
        next_note = [1, pitch, length]
        note_table.push(total)
        return next_note
      end
    }
  }
end



#get_next_likley_note is called to get the next note. It takes in the current
#song, a history int, and the probability table. First, the chunk to match is
#parsed from the song list. The chunk to match is a list of size "history," 
#and contains the last notes played. So if history is 3, then the last three 
#notes played compose the chunk to match. Then the index for the final note is 
#found. After this is found, the song is 
#iterated through. History length chunks are removed from the song after the 
#history index has been reached to avoid an index error, and before the last note 
#becase there would be a perfect match that would give no isight to the next not
#played. Each history length chunk is compared and scored relative to the 
#chunk_to_match. The score of the match is added to the value of the respective
#index of the next note after the chunk being compared. After this the total is 
#updated for the probability array. To put this more simply, let us assing varribls
#x is the objectivly last note played
#y is the chunk being compared to chunk_to_match
#z is the note following the y chunk of notes
#
#a score for y is determined 
#this score is added to the index that corosponds with z in x's probability 
#table. 
#
#This makes for the next note selected to be proportional to the frequecy of its
#occurcne after simmilar chains of notes to the one recentally played
#after the probabiliy matrix is compleatly updated, the next note is selected
#This note allong with the updated probability array are returned and will be 
#fed back into the funtion
def get_next_likley_note(song, history, probability_table)
  #find last note and preceding x notes
  chunk_to_match = song.drop(song.length - history)
  #the note that we are at and need to edit it's prob array
  index_to_edit = get_note_index(song[song.length - 1])
  
  song.each_with_index {|val, index|
    if index >= chunk_to_match.length and index < song.length - 1
      start_index = index - history
      observed_chunk = song[start_index, history]
      score = score_chunk(observed_chunk, chunk_to_match)
      x = ["C3", "D3", "E3", "F3", "G3", "A3", "B3", "Rest"].find_index(song[index][1])
      y = [1,2,4,8].find_index(song[index][2])
      probability_table[index_to_edit][x][y] += score
      probability_table[index_to_edit][8] += score
    end
  }
  next_note = choose_next_note(probability_table[index_to_edit])
  return next_note, probability_table
end

#Make chord takes a note for what chord should be played. Then, the index on the
#G-Major scale is found for that note, if it's a rest then a random number is 
#selected. After that a random num 0-3 is generated, and it's value corrosponds
#to what chord is made. 
#If it's less than 1: a simple 1,3,5 chord
#If 1 < x > 2 than:   5, 1, 3 chord. The 5th is dropped down
#If 2 < x > 3 than:   3, 5, 1 chord, and the 1 is made high
#These are all selected by adding or subtracting from the base note.
#after the numarical representation of a chord has been made,
#then the it is converted into a letter chord by indexing the notes list.
#if because the chord bridges an octave the numeric representation is out of the 
#list range, than eiher 7 is added or subtracted, and the octave number is changed
#acordingly. This produces variation in chords that bridge octaves. The Chord 
#produced is returned.
# 
def make_chord(n_chord)
  chord_list = []
  notes = ["C", "D", "E", "F", "G", "A", "B"]
  note_num = notes.find_index(n_chord)
  if n_chord == "R"
    note_num = rand_i(0..6)
  end
  type_of_chord = rand(0..3)
  base_octave = 3
  chord_list = []
  if type_of_chord < 1
    chord_list = [note_num, note_num + 2, note_num + 4]
  elsif type_of_chord < 2
    chord_list = [note_num - 3, note_num, note_num + 2]
  else
    chord_list = [note_num + 2, note_num + 4, note_num + 7]
  end
  chord_list.each_index do |i|
    if chord_list[i] > 6
      chord_list[i] = notes[(chord_list[i] - 7)] + (base_octave + 1).to_s
    elsif chord_list[i] < 0
      chord_list[i] = notes[(chord_list[i] + 7)] + (base_octave - 1).to_s
    else
      chord_list[i] = notes[chord_list[i]] + base_octave.to_s
    end
  end
  return chord_list
  
end








#This funtion's implementation was complex, but in theroy is very simple. It 
#either maes a chord or rests at the same time a not is played. The chord made
#matches the note from the melody. The difficult part however is that the chord
#length may not match the melody not length, so reset may be added so that the
#next time a chord is up for playing it will occor at the same time as a note.
#This is done by tracking wheather the chord is still being played. A value (chord_done_playing)holds
#the length of the chord, and immidiatly the length of the corrospoding melody
#note is subtracted from the chord note length. Thus if the melody out plays
#the chord than chord_done_playing is negative. This tells the program to 
#possibly play a chord on the next note. First though it needs to add rest to 
#match how much longer the melody played than the chord. This is equivilent to
#the abs of chord_done_playing. If the chord, however, outplays the melody note,
#then every future melody notes length is subtracted from the remained time left
#on chord_done_playing. Once it goes negative, rest is added, and another chord
#is possibly played on the next melody note.
def generate_cord_part(song)
  chord_list = []
  chord_play_history = []
  chord_done_playing = 0
  song.each do |feature|
    chord_to_add = []
    
    ##if chord is done playing or finished exactly on time
    if chord_done_playing <= 0
      play_num = rand()#posibility of resting
      
      #we need to add rest equal to the time until the next note.
      #this rest time is
      if chord_done_playing < 0.0
        chord_list.push([1, "Rest", chord_done_playing.abs()])
        chord_done_playing = 0
      end
      if play_num < 0.7#play a chord
        chord_to_add.push(1)
        chord_to_add.push(make_chord(feature[1][0])) #make a chord of the corrosponding melody note
        chord_length = [2, 1, 0.5, 0.25, 0.125].choose
        chord_to_add.push(chord_length)
        chord_list.push(chord_to_add)
        chord_done_playing = chord_length - (1.0 / feature[2])
      else
        chord_to_add.push(1)
        chord_to_add.push("Rest")
        chord_length = [2, 1, 0.5, 0.25, 0.125].choose
        chord_to_add.push(chord_length)
        chord_list.push(chord_to_add)
        chord_done_playing = chord_length - (1.0 / feature[2])
      end
    else
      chord_done_playing = chord_done_playing - (1.0 / feature[2])
    end
    
  end
  return chord_list
  
end


#Play song goes through a song using a piano, and playes each feature in the song.
#If it is a resting feature, than the peice sleeps for 1/length of rest. If 
#it's not a rest than play the note for the length(1/length), and rest for the 
#same amount of time, so the next note does not start on top of it
#
def play_song(song)
  use_synth :piano
  song.each do |feature|
    if feature.at(1) != "Rest"
      notey = feature.at(1)
      #puts notey
      length = 1.0 / feature.at(2)
      #puts length
      play notey, sustain: length
      sleep length
    else
      sleep 1.0 / feature.at(2)
    end
  end
end

#This method playes the chord part of the song. It goes through each element,
#and if it's a rest, rests lenght of the rest. Becase chord rest was already 
#done in fration there is no division here. If not a rest, than playe the three 
#notes at half volume and sustain for their length. Sleep for length. 
#
#
#
def play_chord_part(chord_part)
  chord_part.each do |chord|
    puts chord
    if chord[1] == "Rest"
      puts "Rest"
      sleep chord[2]
    else
      play chord[1][0], sustain: chord[2], amp: 0.5
      play chord[1][1], sustain: chord[2], amp: 0.5
      play chord[1][2], sustain: chord[2], amp: 0.5
      sleep chord[2]
    end
  end
end

#Main calls all the setup funtions, than randomly generates a inital phrase size
#It then builds the song 600 times over. The chord section is built off of this 
#the melody section, and the both are played on seperate threds
#
def main()
  song_list = Array.new()
  use_random_seed rand(1..99)
  pattern_length = rand(10..16)#Random phrase length
  prob_matrix = make_probability_matrix()
  use_synth :piano
  
  song_list = make_prase(pattern_length)#make starting phrase
  
  600.times do
    next_note, prob_matrix = get_next_likley_note(song_list, 10, prob_matrix)
    song_list.push(next_note)
  end
  
  
  chordy = generate_cord_part(song_list)
  puts song_list
  puts chordy
  in_thread do
    play_song(song_list)
  end
  in_thread do
    play_chord_part(chordy)
  end
  
  
end


main()



