-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 20, 2020 at 06:09 PM
-- Server version: 10.1.39-MariaDB
-- PHP Version: 7.3.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `library`
--

-- --------------------------------------------------------

--
-- Table structure for table `book`
--

CREATE TABLE `book` (
  `id` varchar(20) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `author` varchar(150) DEFAULT NULL,
  `edition` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `book`
--

INSERT INTO `book` (`id`, `name`, `author`, `edition`) VALUES
('B001', 'xxx', 'author1', 'rrr'),
('B002', 'rrr', 'eee', 'hhh'),
('B003', 'qqq', 'qqq', 'wwe'),
('B004', 'kkk', 'auth3', 'jjj');

-- --------------------------------------------------------

--
-- Table structure for table `book_return`
--

CREATE TABLE `book_return` (
  `borrow_id` varchar(20) NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
-- Error reading data for table library.book_return: #1064 - You have an error in your SQL syntax; check the manual that corresponds to your MariaDB server version for the right syntax to use near 'FROM `library`.`book_return`' at line 1

-- --------------------------------------------------------

--
-- Table structure for table `borrow`
--

CREATE TABLE `borrow` (
  `borrow_id` varchar(20) NOT NULL,
  `book_id` varchar(15) NOT NULL,
  `date` date NOT NULL,
  `member_id` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `borrow`
--

INSERT INTO `borrow` (`borrow_id`, `book_id`, `date`, `member_id`) VALUES
('001', 'B001', '2020-03-01', 'M001'),
('002', 'B002', '2020-03-02', 'M002'),
('003', 'B003', '2020-03-04', 'M003'),
('004', 'B003', '2020-03-06', 'M002'),
('005', 'B002', '2020-04-02', 'M007');

-- --------------------------------------------------------

--
-- Table structure for table `fee`
--

CREATE TABLE `fee` (
  `borrow_id` varchar(20) NOT NULL,
  `amount` int(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `fee`
--

INSERT INTO `fee` (`borrow_id`, `amount`) VALUES
('001', 300),
('002', 280),
('003', 200),
('004', 470);

-- --------------------------------------------------------

--
-- Table structure for table `member`
--

CREATE TABLE `member` (
  `id` varchar(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `contact` varchar(12) NOT NULL,
  `address` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `member`
--

INSERT INTO `member` (`id`, `name`, `contact`, `address`) VALUES
('M001', 'dfg', '84859', 'nffm'),
('M002', 'yyy', '084738', 'nmfm'),
('M003', '9494', '9494', 'kkdk'),
('M004', '94940', '94940', 'jfkf'),
('M005', '95959', '95959', 'kkfgkg'),
('M006', '97590', '97590', 'kflg;t'),
('M007', '344', '344', 'yyy');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `book_return`
--
ALTER TABLE `book_return`
  ADD PRIMARY KEY (`borrow_id`);

--
-- Indexes for table `borrow`
--
ALTER TABLE `borrow`
  ADD PRIMARY KEY (`borrow_id`,`book_id`),
  ADD KEY `book_id` (`book_id`),
  ADD KEY `borrow_ibfk_2` (`member_id`);

--
-- Indexes for table `fee`
--
ALTER TABLE `fee`
  ADD PRIMARY KEY (`borrow_id`);

--
-- Indexes for table `member`
--
ALTER TABLE `member`
  ADD PRIMARY KEY (`id`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `book_return`
--
ALTER TABLE `book_return`
  ADD CONSTRAINT `book_return_ibfk_1` FOREIGN KEY (`borrow_id`) REFERENCES `borrow` (`borrow_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `borrow`
--
ALTER TABLE `borrow`
  ADD CONSTRAINT `borrow_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `borrow_ibfk_2` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `fee`
--
ALTER TABLE `fee`
  ADD CONSTRAINT `fee_ibfk_1` FOREIGN KEY (`borrow_id`) REFERENCES `book_return` (`borrow_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
