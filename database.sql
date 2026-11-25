-- =====================================================
-- Database Setup Script
-- =====================================================

-- Create the database if it does not already exist
CREATE DATABASE IF NOT EXISTS myproject;

-- Select the database
USE myproject;

-- Create the account table
CREATE TABLE IF NOT EXISTS account (
    accountnum VARCHAR(18) PRIMARY KEY,
    balance DECIMAL(15,2) NOT NULL,
    holderage INT NOT NULL,
    holdername VARCHAR(50) NOT NULL
);
