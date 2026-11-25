# BankManagementDB

A console-based Java banking application that uses JDBC and MySQL to store and manage bank account records.

## Features

- Create bank accounts with validated account numbers and holder details.
- View all accounts and search by account number.
- Deposit and withdraw money.
- Transfer money between accounts.
- Delete accounts only when their balance is zero.
- Check for sufficient funds during withdrawals and transfers.

## Technologies Used

- Java (JDK 25 LTS)
- JDBC
- MySQL
- MySQL Connector/J
- Visual Studio Code

## Requirements

- Windows 11
- JDK 25 installed and configured.
- MySQL Server installed and running.
- MySQL Connector/J JDBC driver.

## Database Setup

The application uses a MySQL database named `myproject` and a table named `account`.

Execute the provided `database.sql` script in MySQL Workbench or your MySQL command-line client.

### Account Table Structure

| Column | Data Type | Constraint |
|---|---|---|
| accountnum | VARCHAR(18) | PRIMARY KEY |
| balance | DECIMAL(15,2) | NOT NULL |
| holderage | INT | NOT NULL |
| holdername | VARCHAR(50) | NOT NULL |

## Database Configuration

The application uses environment variables for database connection details.

Configure the following variables:

| Variable | Example |
|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/myproject` |
| `DB_USER` | `root` |
| `DB_PASSWORD` | `your_mysql_password` |

For the current PowerShell session, you can set them using:

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3306/myproject"
$env:DB_USER = "root"
$env:DB_PASSWORD = "your_mysql_password"
```
*Note1:* Replace the example username and password with your own MySQL credentials.
*Note2:* These environment variables are available only in the current PowerShell session. If you close the terminal, you must configure them again in a new session.

## MySQL Connector/J Setup

Download MySQL Connector/J from the official website:

https://dev.mysql.com/downloads/connector/j/

Extract the downloaded archive and place the Connector/J `.jar` file inside the project's `lib` folder.
The library is not included in this repository and must be downloaded separately.

## How to Run

Open PowerShell in the project root directory.

Before compilation, create the required folders if they don't already exist:

```powershell
New-Item -ItemType Directory -Force bin, lib
```

Replace `mysql-connector-j-X.Y.Z.jar` with the actual name of your downloaded JAR file and put it inside lib.

**Compile:**

```powershell
javac -cp "lib\mysql-connector-j-X.Y.Z.jar" -d bin src\*.java
```

**Run:**

```powershell
java -cp "bin;lib\mysql-connector-j-X.Y.Z.jar" Main
```

Make sure MySQL Server is running and the database environment variables are configured before starting the application.

## Project Structure

BankManagementDB/
│
├── src/
│   ├── Account.java
│   ├── AccountDAO.java
│   ├── Bank.java
│   ├── DBConnection.java
│   ├── Main.java
│   └── ValidationUtil.java
│
├── database.sql
├── README.md
└── .gitignore


## Validation Rules

- Account numbers must contain 9–18 digits.
- Account numbers must not start with zero.
- Account numbers must be unique.
- Accounts can only be deleted when their balance is zero.
- Withdrawals and transfers require sufficient funds.

## Purpose

This project was developed for learning and practicing Core Java, JDBC, MySQL connectivity, and database transaction management.

This project is a database-based version of my earlier project https://github.com/NamanDevelops/bankmanagement-system-filehandling.git, redesigned to use JDBC and MySQL for persistent data storage.