# OOP_2023
## DictionaryBasic (English - Vietnamese)
A simple dictionary with basic functions for learning purpose. 

Provide a simple GUI for user to interact with the dictionary.

Written in Java with JavaFX. Serve as a learning project for OOP course (INT2204 1).

### Table of contents
- [DictionaryBasic (English - Vietnamese)](#dictionarybasic-english---vietnamese)
  - [Table of contents](#table-of-contents)
  - [Authors](#contributors)
  - [Features](#features)
  - [Installation](#installation)
  - [Usage](#usage)
  - [Contributing](#contributing)
  - [License](#license)
    
### Authors
- [Lý Hồng Đức](https://github.com/lyhongduc123) - 22021217
- [Trịnh Quốc Khánh](https://github.com/khanhcy) - 22021204
- [Hoàng Lê Kim Long](https://github.com/longhoanglekim) - 22021216

### Features
- Search for a word
- Add a new word
- Edit an existing word
- Delete an existing word
- Add words to bookmark/favourite list
- Remove a word from bookmark/favourite list
- History of looked up words
- Sort the dictionary by alphabet
- Using GOOGLE TRANSLATE API to translate paragraph (English - Vietnamese both ways)
- Using GOOGLE TEXT TO SPEECH API to speak the word (English - Vietnamese both ways)
- Save the dictionary to a file/direcly modify the database
- Load the dictionary from a file/database
- Import the dictionary from a .txt file
- Export the dictionary to a .txt file
- Game: Guess the word - Hangman
- Flashcard: Learn new words

### Installation
- Clone the repository
```sh
git clone https://github.com/longhoanglekim/OOP_2023.git
```
- Direct to the project folder
```sh
cd OOP_2023
```
- Not supported yet. Will be updated soon.

### Database Structure
- The database is stored in a .txt file with the following structure:
```txt
@word /pronounce/
definition

@word2 /pronounce2/
definition2
```

- MYSQL structure should be:
```sql
CREATE TABLE `dictionary` (
  `index` int NOT NULL AUTO_INCREMENT,
  `word` varchar(255) NOT NULL,
  `pronounce` varchar(255) DEFAULT NULL,
  `definition` longtext DEFAULT NULL,
  PRIMARY KEY (`index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```
  | Word      | Pronounce | Definition                    |
  |:----------|:----------|:------------------------------|
  | `Varchar` | `Varchar` | `All partofSpeech/definition` |

### Usage
- Run the program
- Use the GUI to interact with the dictionary, customize your own dictionary
- Enjoy!

### Contributing
- Fork the repository
- Make a new branch
- Commit and push your changes
- Create a pull request
- Wait for the pull request to be reviewed and merged
- Thank you for your contribution!

### License
- This is a non-commercial project for learning purpose only.