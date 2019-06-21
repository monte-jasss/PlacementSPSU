-- phpMyAdmin SQL Dump
-- version 4.8.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 21, 2019 at 03:56 PM
-- Server version: 10.1.34-MariaDB
-- PHP Version: 7.2.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_spsu`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_apply`
--

CREATE TABLE `tbl_apply` (
  `id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  `job_id` int(11) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_apply`
--

INSERT INTO `tbl_apply` (`id`, `student_id`, `job_id`, `status`) VALUES
(1, 2, 1, 0),
(3, 2, 2, 1),
(4, 2, 3, 1),
(5, 2, 4, 0),
(6, 2, 5, 0),
(8, 3, 1, 1),
(9, 3, 2, 0),
(10, 3, 4, 0),
(11, 3, 3, 0),
(12, 3, 5, 1),
(15, 3, 6, 0);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_job`
--

CREATE TABLE `tbl_job` (
  `id` int(11) NOT NULL,
  `company_id` int(11) NOT NULL,
  `title` varchar(500) NOT NULL,
  `description` varchar(700) NOT NULL,
  `requirement` varchar(500) NOT NULL,
  `opening` int(4) NOT NULL,
  `lastdate` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_job`
--

INSERT INTO `tbl_job` (`id`, `company_id`, `title`, `description`, `requirement`, `opening`, `lastdate`) VALUES
(1, 5, 'Android Developer', 'skilled and experienced in java and android studio', 'Android studio, java, xml', 3, '30/04/2019'),
(2, 5, 'Java Developer', 'skilled and experienced in java and android studio', 'Core Java, Advance Java', 5, '26/04/2019'),
(3, 6, 'Web Developer', 'skilled and experienced in HTML,CSS,PHP,JS and AJAX', 'HTML,CSS,PHP,JS and AJAX', 8, '29/04/2019'),
(4, 6, 'Machine Learning', 'skilled and experienced in artificial intelligence', 'prolog, matlab', 2, '30/04/2019'),
(5, 7, 'IOS', 'skilled and experienced in Swift and xcode', 'Swift and xcode', 3, '26/04/2019'),
(6, 5, 'Abc', 'xyz', 'btech', 0, '22/04/2019');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_profile`
--

CREATE TABLE `tbl_profile` (
  `id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  `name` varchar(500) NOT NULL,
  `f_name` varchar(500) NOT NULL,
  `course` varchar(100) NOT NULL,
  `semester` int(2) NOT NULL,
  `_10` float NOT NULL,
  `_12` float NOT NULL,
  `cgpa` varchar(10) NOT NULL,
  `interest` varchar(200) NOT NULL,
  `skill` varchar(5000) NOT NULL,
  `project` varchar(5000) NOT NULL,
  `training` varchar(5000) NOT NULL,
  `certificate` varchar(5000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_profile`
--

INSERT INTO `tbl_profile` (`id`, `student_id`, `name`, `f_name`, `course`, `semester`, `_10`, `_12`, `cgpa`, `interest`, `skill`, `project`, `training`, `certificate`) VALUES
(1, 2, 'Ashwani Lakshkar', 'Mukesh Kumar Lakshmar', 'B.tech', 8, 7, 55, '5.7', 'Technical', 'Java, android studio, PHP, html,', 'placement cell app', 'infinite mobility, Delhi', 'none'),
(2, 3, 'Monu Lakshkar', 'Mahesh Chandra', 'B.tech(CSE)', 8, 84, 92, '8.1', 'Technical', 'Java\nAndroid\nC\nC++\nPHP\nHTML\nCSS\nJavaScript', 'Placement app\nNotice app\nBus booking website\nMentorship portal\nSPSU website', 'ValeurHR Chandigarh', 'Android training');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_user`
--

CREATE TABLE `tbl_user` (
  `id` int(11) NOT NULL,
  `name` varchar(200) NOT NULL,
  `email` varchar(500) NOT NULL,
  `mobile` bigint(10) NOT NULL,
  `password` varchar(32) NOT NULL,
  `type` int(1) NOT NULL DEFAULT '0',
  `otp` int(6) NOT NULL,
  `active` int(1) NOT NULL DEFAULT '0',
  `token` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbl_user`
--

INSERT INTO `tbl_user` (`id`, `name`, `email`, `mobile`, `password`, `type`, `otp`, `active`, `token`) VALUES
(1, 'TPO', 'tpo@spsu.ac.in', 1234567890, '4b9db269c5f978e1264480b0a7619eea', 2, 0, 1, ''),
(2, 'Ashwani Lakshkar', 'ashwani.lakshkar@spsu.ac.in', 7742041636, '098f6bcd4621d373cade4e832627b4f6', 0, 0, 1, ''),
(3, 'Monu Lakshkar', 'monu.lakshkar@spsu.ac.in', 8966073336, '12793ea96d6f3660c3b06f4de4497768', 0, 0, 1, ''),
(4, 'PRANSHU PORWAL', 'pranshuporwal@gmail.com', 8769949362, '25d55ad283aa400af464c76d713c07ad', 0, 0, 1, ''),
(5, 'TCS', 'tcs@gmail.com', 9874561320, '098f6bcd4621d373cade4e832627b4f6', 1, 0, 1, ''),
(6, 'WIPRO', 'wipro@gmail.com', 5421348789, '098f6bcd4621d373cade4e832627b4f6', 1, 0, 1, ''),
(7, 'HCL', 'hcl@gmail.com', 5349546185, '098f6bcd4621d373cade4e832627b4f6', 1, 0, 1, '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_apply`
--
ALTER TABLE `tbl_apply`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `student_id` (`student_id`,`job_id`),
  ADD KEY `tbl_apply_ibfk_1` (`job_id`);

--
-- Indexes for table `tbl_job`
--
ALTER TABLE `tbl_job`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `company_id` (`company_id`,`title`,`lastdate`);

--
-- Indexes for table `tbl_profile`
--
ALTER TABLE `tbl_profile`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `student_id` (`student_id`);

--
-- Indexes for table `tbl_user`
--
ALTER TABLE `tbl_user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `mobile` (`mobile`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_apply`
--
ALTER TABLE `tbl_apply`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `tbl_job`
--
ALTER TABLE `tbl_job`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `tbl_profile`
--
ALTER TABLE `tbl_profile`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `tbl_user`
--
ALTER TABLE `tbl_user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tbl_apply`
--
ALTER TABLE `tbl_apply`
  ADD CONSTRAINT `tbl_apply_ibfk_1` FOREIGN KEY (`job_id`) REFERENCES `tbl_job` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `tbl_apply_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `tbl_user` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `tbl_job`
--
ALTER TABLE `tbl_job`
  ADD CONSTRAINT `tbl_job_ibfk_1` FOREIGN KEY (`company_id`) REFERENCES `tbl_user` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `tbl_profile`
--
ALTER TABLE `tbl_profile`
  ADD CONSTRAINT `tbl_profile_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `tbl_user` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
