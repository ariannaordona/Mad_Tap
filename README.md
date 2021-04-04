# Mad_Tap

This is a personal project made to increase my set of vocabulary for learning Mandarin. 

![mad_tap](https://user-images.githubusercontent.com/32204716/113523833-4101c980-955f-11eb-8f43-ae8e76caf4c5.gif)

Using Duolingo can be fast and fun very quickly. But they do not use traditional characters and do not teach anything of the phonetic alphabet of Zhuyin. If you want to type/text in mandarin, you will have to use the phonetic alphabet to type and search for the character you want to input. In addition, when reading a book you will often run into characters you do not recognize and most books will have the phonetic spelling on the side, letting the reader search the word in a dictionary using the phonetic spelling to search.

This app is meant to help me learn mandarin and increase my memorization with traditional characters and their phonetic spellings.

![pause](https://user-images.githubusercontent.com/32204716/113523825-2e879000-955f-11eb-9c24-a265da0122e3.gif)

**Standard Mode**
- Time limit of a minute with a countdown at the start
- Player can pause the game at any time and change settings to show or not show zhuyin and pinyin
- Can pause to restart or quit to main menu

![endless](https://user-images.githubusercontent.com/32204716/113523817-19aafc80-955f-11eb-890a-f4c9bd45ae01.gif)

**Endless Mode**
- No time limit
- Game over when the player enters a wrong answer

![practice](https://user-images.githubusercontent.com/32204716/113523820-24659180-955f-11eb-937f-8c456d307a4d.gif)

**Practice Mode**
- No time limit
- Answers are shown on the right side
- No score is recorded

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

RULES (4 modes)
- Standard: Get as many correct answers within a time limit
- Hard: Time limit and layout of character buttons is shuffled after every correct answer
- Practice: No time limit and the answer is shown on the left side
- Endless: No time limit, but the first wrong answer will end the game

- Traditional / Simplified (Personally I would rather only learn Traditional, not a high priority to implement a simplified option)
- Characters that need more than one to mean something (ie. basketball can be represented as a basketball, but would need more than 1 character). On the interface the card with the basketball image would have a (2) underneath the image showing you need 2 characters

====================================

DATABASE (WIP to connect app to database, currently all hard coded)
- Database needs work, specifically images. Otherwise, code is easy to generate from existing excel and queries are basic
- Database will be MySQL (because I am familiar with it)

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
- Input > # of words (WIP)
- Radio button > Traditional/Symplified
- Radio button > Audio (No audio implemented currently)
- Button > Start

======================================================================

WHAT IS IMPLEMENTED
- Basic working tapping game where there is a list of hard coded characters that are randomly put into the buttons 
- Images are all in the drawable folder, database will store the location
- Upon inputting the correct characters, a quick animation of the next image moves downward, if incorrect quick animation of the image shakes
- All 4 modes are implemented
- A random image is chosen into the list
