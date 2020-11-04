# Mad_Tap

This is a personal project made to increase my set of vocabulary for learning Mandarin. 

MANDARIN MATCH (WIP)

====================================

Choose the correct symbol that matches the upcoming picture
- divided in categories
- choose the number of words you would like to practice with

====================================

LAYOUT
- Top bar - shows time, score, high score
- Bottom bar - shows every character for available answers 3x3 buttons with bottom corner reserved for clear button
- Menu button - go back to categories, restart, or choose a different mode of the game, change settings (traditional/simplified, etc)

====================================

RULES
- infinite mode -> keeps going and going until you die
- add a time limit to challenge player
- can disable time limit just for practicing
- get more points for continuous correct answers
- when getting correct -> play audio file (DEFINITELY CAN GET ANNOYING -> MUST BE ABLE TO DISABLE)
- Traditional / Simplified
- Characters that need more than one to mean something (ie. basketball can be represented as a basketball, but would need more than 1 character). On the interface the card with the basketball image would have a (2) underneath the image showing you need 2 characters

====================================

DATABASE (WIP to connect app to database, current all hard coded)

CHARACTERS
Name
Mandarin 
Symbol (Image)
Audio file (Not Necessary)
category ID

Category
================================

INTERFACE
- Button > Start
- Drop down menu > Category
- Input > # of words
- Radio button > Traditional/Symplified
- Radio button > Audio
- Button > Start

=== Randomize words in category ====
- Continually show words (keep looping/repeating words)
- Many symbols/images on the bottom of screen (near thumb)
 - Player taps symbols/images to give answer -> what the above word matches to image
- If player gets it wrong============
 - screen pops a big X on screen
 - pauses the game for 1 second
- If player gets it right===========
 - images go down
 - screen pops large image of correct symbol onto screen 
 - Score updates

======================================================================

WHAT IS IMPLEMENTED
- Basic working tapping game where there is a list of hard coded characters that are randomly put into the buttons 
- Set of images are hardcoded and saved into the drawable folder
- Upon inputting the correct characters, a quick animation of the next image moves downward
- A random image is chosen into the list, the answer is already shown on the right side of the image (will be implemented as showing answer toggle for a practice mode)
