-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Aug 11, 2022 at 07:48 AM
-- Server version: 10.5.13-MariaDB-cll-lve
-- PHP Version: 7.2.34

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `u122808950_tobasend`
--

-- --------------------------------------------------------

--
-- Table structure for table `catatan_order`
--

CREATE TABLE `catatan_order` (
  `id` int(11) NOT NULL,
  `id_order_kendaraan` int(11) NOT NULL,
  `id_pengaju` int(11) NOT NULL,
  `pengaju` varchar(255) NOT NULL,
  `harga` bigint(20) NOT NULL,
  `catatan` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `catatan_order`
--

INSERT INTO `catatan_order` (`id`, `id_order_kendaraan`, `id_pengaju`, `pengaju`, `harga`, `catatan`, `created_at`, `updated_at`) VALUES
(6, 70, 1, 'supir', 255000, 'naik', '2022-06-25 08:03:22', '2022-06-25 08:03:22'),
(7, 70, 33, 'petani', 25000, 'tes', '2022-06-25 08:03:40', '2022-06-25 08:03:40'),
(8, 70, 1, 'supir', 25500, 'tes', '2022-06-25 08:07:05', '2022-06-25 08:07:05'),
(9, 70, 33, 'petani', 25200, 'tes', '2022-06-25 16:11:59', '2022-06-25 16:11:59'),
(10, 70, 33, 'petani', 25200, 'tes', '2022-06-25 16:12:13', '2022-06-25 16:12:13'),
(11, 70, 1, 'supir', 25250, 'cwk', '2022-06-25 16:13:38', '2022-06-25 16:13:38'),
(12, 70, 1, 'supir', 200, 'po', '2022-06-25 16:14:19', '2022-06-25 16:14:19'),
(13, 70, 1, 'supir', 200, 'po', '2022-06-25 16:14:49', '2022-06-25 16:14:49'),
(16, 72, 33, 'petani', 25, 'cek', '2022-06-25 16:46:07', '2022-06-25 16:46:07'),
(17, 72, 33, 'petani', 170000, 'tes', '2022-06-25 16:46:30', '2022-06-25 16:46:30'),
(18, 72, 1, 'supir', 175000, 'vek', '2022-06-25 16:47:28', '2022-06-25 16:47:28'),
(19, 73, 33, 'petani', 80000, 'tes', '2022-06-26 13:28:58', '2022-06-26 13:28:58'),
(20, 75, 33, 'petani', 45000, 'cek', '2022-06-26 13:52:37', '2022-06-26 13:52:37'),
(21, 75, 33, 'petani', 20, 'c', '2022-06-26 13:57:21', '2022-06-26 13:57:21'),
(22, 75, 33, 'petani', 20, 'we', '2022-06-26 13:58:06', '2022-06-26 13:58:06'),
(23, 75, 33, 'petani', 60, 'te', '2022-06-26 13:58:23', '2022-06-26 13:58:23'),
(24, 75, 33, 'petani', 800, 're', '2022-06-26 13:59:50', '2022-06-26 13:59:50'),
(25, 77, 15, 'supir', 120000, 'coba', '2022-06-27 11:37:43', '2022-06-27 11:37:43'),
(26, 77, 33, 'petani', 118000, 'turun', '2022-06-27 11:39:36', '2022-06-27 11:39:36');

-- --------------------------------------------------------

--
-- Table structure for table `failed_jobs`
--

CREATE TABLE `failed_jobs` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `uuid` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `connection` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `queue` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `payload` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `exception` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `failed_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `hasil_tani`
--

CREATE TABLE `hasil_tani` (
  `id_hasil_panen` int(11) NOT NULL,
  `id_petani` int(11) NOT NULL,
  `berat_barang` varchar(255) NOT NULL,
  `harga` varchar(255) NOT NULL DEFAULT '0',
  `jenis_hasil_tani` varchar(255) NOT NULL,
  `img` varchar(255) NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `hasil_tani`
--

INSERT INTO `hasil_tani` (`id_hasil_panen`, `id_petani`, `berat_barang`, `harga`, `jenis_hasil_tani`, `img`, `updated_at`, `created_at`) VALUES
(50, 33, '5000', '6500', 'Bawang', '16556220411655622042704_image.PNG', NULL, NULL),
(51, 33, '5000', '8000', 'Bawang2', '16566428181656642819964_image.PNG', '2022-07-01 02:33:38', NULL),
(62, 33, '500', '2500', 'Tomat', '16558208701655820870306_image.PNG', NULL, NULL),
(65, 37, '10', '3000000', 'Salmon', '16564018261656401817578_image.PNG', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `kendaraan`
--

CREATE TABLE `kendaraan` (
  `id_kendaraan` int(10) NOT NULL,
  `id_supir` int(10) NOT NULL,
  `jenis_kendaraan` varchar(255) DEFAULT NULL,
  `muatan_kendaraan` varchar(255) DEFAULT NULL,
  `muatan_terpenuhi` int(11) NOT NULL DEFAULT 0,
  `stat` varchar(255) NOT NULL,
  `lokasi_awal` varchar(255) DEFAULT NULL,
  `lokasi_akhir` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `kendaraan`
--

INSERT INTO `kendaraan` (`id_kendaraan`, `id_supir`, `jenis_kendaraan`, `muatan_kendaraan`, `muatan_terpenuhi`, `stat`, `lokasi_awal`, `lokasi_akhir`, `updated_at`, `created_at`) VALUES
(1, 5, 'Pickup', '1500', 0, 'Tersedia', 'Sitoluama', 'Siantar', '2022-07-29 03:56:33', '2022-04-13 08:21:02'),
(15, 35, 'Truk', '1500', 1002, 'Tersedia', '', '', '2022-06-27 11:40:04', NULL),
(16, 38, 'Pickup', '1500', 0, 'Tersedia', '', '', '2022-06-28 07:39:35', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `migrations`
--

CREATE TABLE `migrations` (
  `id` int(10) UNSIGNED NOT NULL,
  `migration` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `batch` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `migrations`
--

INSERT INTO `migrations` (`id`, `migration`, `batch`) VALUES
(1, '2014_10_12_000000_create_users_table', 1),
(2, '2014_10_12_100000_create_password_resets_table', 1),
(3, '2019_08_19_000000_create_failed_jobs_table', 1);

-- --------------------------------------------------------

--
-- Table structure for table `order_kendaraan`
--

CREATE TABLE `order_kendaraan` (
  `id_order` int(11) NOT NULL,
  `alamat` varchar(255) NOT NULL,
  `id_kendaraan` int(10) NOT NULL,
  `id_petani` int(10) NOT NULL,
  `status_pembayaran` varchar(255) NOT NULL,
  `bukti_pembayaran` varchar(255) DEFAULT NULL,
  `berat_bawaan` int(10) NOT NULL,
  `harga` int(10) NOT NULL,
  `harga_awal` bigint(20) NOT NULL DEFAULT 0,
  `lokasi_awal` varchar(255) DEFAULT NULL,
  `lokasi_tujuan` varchar(255) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `pengaju` varchar(255) DEFAULT NULL,
  `status_pengajuan` varchar(255) DEFAULT NULL,
  `bukti_selesai` varchar(255) DEFAULT NULL,
  `konfirmasi_selesai` varchar(255) DEFAULT NULL,
  `catatan` varchar(355) DEFAULT NULL,
  `tanggal_penjmputan` date DEFAULT NULL,
  `durasi_pengataran` int(11) NOT NULL DEFAULT 0,
  `waktu_berangkat` datetime DEFAULT NULL,
  `waktu_selesai` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `order_kendaraan`
--

INSERT INTO `order_kendaraan` (`id_order`, `alamat`, `id_kendaraan`, `id_petani`, `status_pembayaran`, `bukti_pembayaran`, `berat_bawaan`, `harga`, `harga_awal`, `lokasi_awal`, `lokasi_tujuan`, `status`, `pengaju`, `status_pengajuan`, `bukti_selesai`, `konfirmasi_selesai`, `catatan`, `tanggal_penjmputan`, `durasi_pengataran`, `waktu_berangkat`, `waktu_selesai`, `created_at`, `updated_at`) VALUES
(70, 'Jl. Y.P. Arjuna No.120, Pintu Bosi, Kec. Laguboti, Toba, Sumatera Utara, Indonesia', 1, 33, 'Proses', 'Tidak ada', 1320, 2900, 2900, 'Indonesia, Sumatera Utara, Sumatera Utara, 22381, Kecamatan Laguboti', 'Indonesia, Sumatera Utara, Sumatera Utara, 22384, Kecamatan Sigumpar', 'batalkan', 'supir', 'proses', NULL, NULL, 'es', '2022-06-25', 11, NULL, NULL, '2022-06-25 07:27:09', '2022-06-26 13:27:40'),
(72, 'Jl. Y.P. Arjuna No.120, Pintu Bosi, Kec. Laguboti, Toba, Sumatera Utara, Indonesia', 1, 33, 'Proses', 'Tidak ada', 1320, 175000, 182733, 'Indonesia, Sumatera Utara, Sumatera Utara, 22381, Kecamatan Laguboti', 'Indonesia, Sumatera Utara, Sumatera Utara, 22384, Kecamatan Sigumpar', 'batalkan', 'supir', 'proses', NULL, NULL, 'vek', '2022-06-26', 14, NULL, NULL, '2022-06-25 16:42:27', '2022-06-26 13:28:15'),
(73, 'Jl. Y.P. Arjuna No.120, Pintu Bosi, Kec. Laguboti, Toba, Sumatera Utara, Indonesia', 1, 33, 'Proses', 'Tidak ada', 250, 80000, 83946, 'Indonesia, Sumatera Utara, Sumatera Utara, null, Kecamatan Laguboti', 'Indonesia, Sumatera Utara, Sumatera Utara, 22384, Kecamatan Porsea', 'batalkan', 'petani', 'proses', NULL, NULL, 'tes', '2022-06-27', 22, NULL, NULL, '2022-06-26 13:28:40', '2022-06-26 13:29:11'),
(74, 'Jl. Y.P. Arjuna No.120, Pintu Bosi, Kec. Laguboti, Toba, Sumatera Utara, Indonesia', 1, 33, 'Proses', 'Tidak ada', 2500, 280806, 0, 'Indonesia, Sumatera Utara, Sumatera Utara, null, Kecamatan Laguboti', 'Indonesia, Sumatera Utara, Sumatera Utara, 22381, Kecamatan Laguboti', 'batalkan', NULL, NULL, NULL, NULL, NULL, '2022-06-27', 9, NULL, NULL, '2022-06-26 13:31:34', '2022-06-26 13:48:56'),
(75, 'Jl. Y.P. Arjuna No.120, Pintu Bosi, Kec. Laguboti, Toba, Sumatera Utara, Indonesia', 1, 33, 'Proses', 'proses', 250, 800, 50490, 'Indonesia, Sumatera Utara, Sumatera Utara, null, Kecamatan Laguboti', 'Indonesia, Sumatera Utara, Sumatera Utara, 22381, Kecamatan Laguboti', 'Proses', 'petani', 'selesai', NULL, NULL, 're', '2022-06-26', 9, NULL, NULL, '2022-06-26 13:51:43', '2022-06-30 07:36:27'),
(76, '94PX+922, Sitoluama, Kec. Laguboti, Toba, Sumatera Utara, Indonesia', 1, 33, 'Terima Bukti', '16562789011656278899457_image.PNG', 2000, 598536, 0, 'Indonesia, Sumatera Utara, Sumatera Utara, null, Simanindo', 'Indonesia, Sumatera Utara, Sumatera Utara, 21145, Kecamatan Siantar Barat', 'Selesai', NULL, 'selesai', '16562790131656278998533_image.PNG', 'Selesai', NULL, '2022-06-29', 152, NULL, NULL, '2022-06-26 21:25:10', '2022-06-27 13:41:19'),
(77, 'Jl. Y.P. Arjuna No.120, Pintu Bosi, Kec. Laguboti, Toba, Sumatera Utara, Indonesia', 15, 33, 'Terima Bukti', '16563301611656330164590_image.PNG', 1002, 118000, 111227, 'Indonesia, Sumatera Utara, Sumatera Utara, null, Kecamatan Laguboti', 'Indonesia, Sumatera Utara, Sumatera Utara, null, Kecamatan Bonatua Lunasi', 'Di terima supir', 'petani', 'selesai', '16563302851656330288944_image.PNG', 'Menunggu Persetujuan', 'turun', '2022-06-28', 36, NULL, NULL, '2022-06-27 11:30:33', '2022-06-27 11:44:45'),
(78, 'Jl. Y.P. Arjuna No.120, Pintu Bosi, Kec. Laguboti, Toba, Sumatera Utara, Indonesia', 1, 33, 'Proses', 'Tidak ada', 1500, 150475, 0, 'Indonesia, Sumatera Utara, Sumatera Utara, null, Kecamatan Laguboti', 'Indonesia, Sumatera Utara, Sumatera Utara, 22386, Kecamatan Bonatua Lunasi', 'Proses', NULL, NULL, NULL, NULL, NULL, '2022-07-02', 26, NULL, NULL, '2022-07-01 07:55:51', '2022-07-01 07:55:51'),
(79, 'Jl. Y.P. Arjuna No.120, Pintu Bosi, Kec. Laguboti, Toba, Sumatera Utara, Indonesia', 1, 33, 'Proses', 'Tidak ada', 200, 14666, 0, 'laguboti', 'balige', 'Proses', NULL, NULL, NULL, NULL, NULL, '2022-07-11', 0, NULL, NULL, '2022-07-11 03:19:39', '2022-07-11 03:19:39'),
(80, 'Jl. Y.P. Arjuna No.120, Pintu Bosi, Kec. Laguboti, Toba, Sumatera Utara, Indonesia', 15, 33, 'Proses', 'Tidak ada', 200, 14666, 0, 'Indonesia, Sumatera Utara, Sumatera Utara, null, Kecamatan Laguboti', 'Indonesia, Sumatera Utara, Sumatera Utara, null, Kecamatan Laguboti', 'Proses', NULL, NULL, NULL, NULL, NULL, '2022-07-11', 0, NULL, NULL, '2022-07-11 03:29:44', '2022-07-11 03:29:44');

-- --------------------------------------------------------

--
-- Table structure for table `password_resets`
--

CREATE TABLE `password_resets` (
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pelabuhan`
--

CREATE TABLE `pelabuhan` (
  `id` int(11) NOT NULL,
  `nama_pelabuhan` varchar(255) NOT NULL,
  `lat` double NOT NULL,
  `lon` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `pelabuhan`
--

INSERT INTO `pelabuhan` (`id`, `nama_pelabuhan`, `lat`, `lon`) VALUES
(1, 'Tigaras Harbor', 2.798096, 98.789051),
(2, 'Pelabuhan Simanindo', 2.754405, 98.746154);

-- --------------------------------------------------------

--
-- Table structure for table `personal_access_tokens`
--

CREATE TABLE `personal_access_tokens` (
  `id` int(10) UNSIGNED NOT NULL,
  `tokenable_type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tokenable_id` int(10) UNSIGNED NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `abilities` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_used_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `personal_access_tokens`
--

INSERT INTO `personal_access_tokens` (`id`, `tokenable_type`, `tokenable_id`, `name`, `token`, `abilities`, `last_used_at`, `created_at`, `updated_at`) VALUES
(26, 'App\\Models\\User', 28, 'MyApp', 'fc50558cc94c6f258cf2cf362d6652e2cda866657fb6a7db8504387ce904d16f', '[\"*\"]', NULL, '2022-06-09 20:21:43', '2022-06-09 20:21:43'),
(27, 'App\\Models\\User', 29, 'MyApp', '074d9de1f64355d7e850bbef8f71ccad2cc99649e9caa97c024ed30216259e46', '[\"*\"]', NULL, '2022-06-09 20:26:13', '2022-06-09 20:26:13'),
(28, 'App\\Models\\User', 30, 'MyApp', 'd1e5c3baad7d35ce5d1b4fbc1377416e1aa8799569355ba49241e1fe060d1e88', '[\"*\"]', NULL, '2022-06-09 20:26:21', '2022-06-09 20:26:21'),
(29, 'App\\Models\\User', 31, 'MyApp', 'd777aa340efc203e32f8a768a9372ca60d57751bfcd31df6b2d0f2ba54ffa19c', '[\"*\"]', NULL, '2022-06-09 20:26:40', '2022-06-09 20:26:40'),
(30, 'App\\Models\\User', 32, 'MyApp', '0efa8e43ff60be7e835f99ce4cd2d072b8d0149850e905b8551c93f4f2364084', '[\"*\"]', NULL, '2022-06-09 20:27:16', '2022-06-09 20:27:16'),
(31, 'App\\Models\\User', 33, 'MyApp', '63a9ac271bf0ddf12160335cc8d324f920dff923b9f8ea93a0c513abcebb471e', '[\"*\"]', NULL, '2022-06-15 07:14:33', '2022-06-15 07:14:33'),
(32, 'App\\Models\\User', 35, 'MyApp', '1374ebc2151cf1c56c20aec65b0d5face1434fe04457c114ededd18a4ed457f3', '[\"*\"]', NULL, '2022-06-20 09:12:26', '2022-06-20 09:12:26'),
(33, 'App\\Models\\User', 36, 'MyApp', '529705fdfdc1ea7138c127ff77af6b72f200232291d8b9f8f49cbacf333a4d33', '[\"*\"]', NULL, '2022-06-27 22:40:41', '2022-06-27 22:40:41'),
(34, 'App\\Models\\User', 37, 'MyApp', '5704565177eb23e2cf45815dc9cb94967296ff9d0874487f5734e1651b4aba8b', '[\"*\"]', NULL, '2022-06-28 07:34:31', '2022-06-28 07:34:31'),
(35, 'App\\Models\\User', 38, 'MyApp', '662d173bc485547237556b25427aa0817217ec23de60702fb5331e55384229dd', '[\"*\"]', NULL, '2022-06-28 07:38:36', '2022-06-28 07:38:36');

-- --------------------------------------------------------

--
-- Table structure for table `supir`
--

CREATE TABLE `supir` (
  `id_supir` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `no_telepone` varchar(255) DEFAULT NULL,
  `nama_supir` varchar(255) NOT NULL,
  `poto_supir` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `track_lokasi`
--

CREATE TABLE `track_lokasi` (
  `id` int(11) NOT NULL,
  `id_order` int(11) NOT NULL,
  `lokasi_saat_ini` varchar(255) NOT NULL,
  `lan` double NOT NULL,
  `lat` double NOT NULL,
  `catatan` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `track_lokasi`
--

INSERT INTO `track_lokasi` (`id`, `id_order`, `lokasi_saat_ini`, `lan`, `lat`, `catatan`, `created_at`) VALUES
(44, 37, 'Jalan Tanpa Nama, Kec. Bonatua Lunasi, Toba, Sumatera Utara 22386, Indonesia', 2.501051, 99.1311691, 'IN DEV', '2022-06-17 02:46:50'),
(45, 37, 'Jalan Tanpa Nama, Kec. Bonatua Lunasi, Toba, Sumatera Utara 22386, Indonesia', 2.501051, 99.1311691, 'IN DEV', '2022-06-17 02:46:51'),
(46, 37, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3850704, 99.1502853, 'IN DEV', '2022-06-17 02:49:52'),
(47, 37, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3850704, 99.1502853, 'IN DEV', '2022-06-17 02:52:23'),
(48, 37, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3850704, 99.1502853, 'IN DEV', '2022-06-17 04:34:57'),
(49, 37, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3850704, 99.1502853, 'IN DEV', '2022-06-17 04:35:02'),
(50, 37, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3850704, 99.1502853, 'IN DEV', '2022-06-17 04:47:10'),
(51, 37, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3850704, 99.1502853, 'IN DEV', '2022-06-17 04:47:17'),
(52, 37, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3850704, 99.1502853, 'IN DEV', '2022-06-17 04:47:19'),
(53, 37, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3850704, 99.1502853, 'IN DEV', '2022-06-17 04:47:27'),
(54, 39, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3850704, 99.1502853, 'IN DEV', '2022-06-17 04:47:43'),
(55, 39, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3850704, 99.1502853, 'IN DEV', '2022-06-17 04:47:45'),
(56, 39, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3850704, 99.1502853, 'IN DEV', '2022-06-17 04:47:47'),
(57, 39, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3850704, 99.1502853, 'IN DEV', '2022-06-17 04:47:49'),
(58, 39, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3850704, 99.1502853, 'IN DEV', '2022-06-17 04:47:50'),
(59, 39, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3850704, 99.1502853, 'IN DEV', '2022-06-17 04:47:52'),
(60, 39, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3850704, 99.1502853, 'IN DEV', '2022-06-17 04:47:54'),
(61, 39, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3850704, 99.1502853, 'IN DEV', '2022-06-17 04:47:59'),
(62, 41, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3850911, 99.1503427, 'IN DEV', '2022-06-18 17:13:15'),
(63, 41, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3850911, 99.1503427, 'IN DEV', '2022-06-18 17:13:17'),
(64, 54, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3843094, 99.1494328, 'IN DEV', '2022-06-20 08:36:10'),
(65, 54, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3843094, 99.1494328, 'IN DEV', '2022-06-20 08:36:21'),
(66, 55, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3843094, 99.1494328, 'IN DEV', '2022-06-20 08:50:18'),
(67, 57, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3843094, 99.1494328, 'IN DEV', '2022-06-20 11:51:59'),
(68, 58, '95Q3+X8C, Sigumpar Dangsina, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3893222, 99.1527069, 'IN DEV', '2022-06-20 16:39:15'),
(69, 61, '94PW+CPC, Sitoluama, Kec. Laguboti, Toba, Sumatera Utara, Indonesia', 2.3860003, 99.1468929, 'IN DEV', '2022-06-23 07:08:05'),
(70, 63, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3850995, 99.1503243, 'IN DEV', '2022-06-24 05:58:42'),
(71, 63, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3850995, 99.1503243, 'IN DEV', '2022-06-24 05:58:44'),
(72, 76, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3851024, 99.1503178, 'IN DEV', '2022-06-26 21:27:21'),
(73, 76, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3851024, 99.1503178, 'IN DEV', '2022-06-26 21:27:24'),
(74, 76, '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', 2.3851024, 99.1503178, 'IN DEV', '2022-06-26 21:30:54'),
(75, 75, 'Data tidak ditemukan', 0, 0, 'IN DEV', '2022-06-28 03:08:02');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `no_telepon` varchar(255) NOT NULL,
  `alamat` varchar(255) DEFAULT NULL,
  `poto_profil` varchar(255) DEFAULT NULL,
  `role` varchar(255) NOT NULL,
  `lat` double NOT NULL DEFAULT 0,
  `lon` double NOT NULL DEFAULT 0,
  `no_tf` varchar(255) DEFAULT NULL,
  `updated_at` datetime NOT NULL,
  `created_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `username`, `password`, `nama`, `no_telepon`, `alamat`, `poto_profil`, `role`, `lat`, `lon`, `no_tf`, `updated_at`, `created_at`) VALUES
(5, 'tes4', '$2y$10$sT8xbDG.GQW7WuON7ROjo.JJywePAxm.gm1HnxhgY2thNvB4RPgiO', 'Permana Sembiring', '0895601299835', '94PX+922, Sitoluama, Kec. Laguboti, Toba, Sumatera Utara, Indonesia', '16557136111655713611693_image.PNG', 'supir', 2.3858392, 99.1474889, '01020339123 - BRI', '2022-07-29 03:56:33', '2022-04-16 10:26:17'),
(33, 'tes', '$2y$10$QetfolD3jvvChSHSCuI5ieqruPBKJoK7arVq3gDR4YAJ8dFT3xS0q', 'Permana Sembiring', '089560124', 'Jl. Y.P. Arjuna No.120, Pintu Bosi, Kec. Laguboti, Toba, Sumatera Utara, Indonesia', '16556305941655630590767_image.PNG', 'petani', 2.38141, 99.1506312, 'null', '2022-06-24 05:53:05', '2022-06-15 14:14:33'),
(35, 'nara', '$2y$10$lPDzJQQ3aMjJLMoAOHL//upz3N5k2Lq0aCl/ECQkbgTS45C9.fKoa', 'Miranti Sinaga', '0895601299835', '94MX+HQR, Jl. Silaen, Sitoluama, Kec. Sigumpar, Toba, Sumatera Utara, Indonesia', '16557792861655779285243_image.PNG', 'supir', 2.3850704, 99.1502853, '250005', '2022-06-27 11:37:09', '2022-06-20 16:12:26'),
(36, 'petani', '$2y$10$/8S9dPP/flP17ALVZAYhw.zWXq/b2IC1Ja2RvEReuwcBoWU22Nuga', 'Petani - Cek', '0895601299835', NULL, NULL, 'petani', 0, 0, NULL, '2022-06-27 22:40:40', '2022-06-27 22:40:40'),
(37, 'petani1', '$2y$10$TCMFA.u/vGQotlnImK.wCOQzstYQM/aSCjcev//g2NfISyW8VWf0O', 'petani1', '085137774602', '8916 Takayamachō, Ikoma, Nara 630-0101, Japan', '16564023351656402331633_image.PNG', 'petani', 34.732242, 135.7342262, 'null', '2022-06-28 07:45:35', '2022-06-28 07:34:30'),
(38, 'supir1', '$2y$10$8zMkvRlLjCS2CG8PQ2lSQuvAAL/f.3r2ZYpwdAdmNWmVli8mcYyn.', 'supir1', '085243333097', '8916 Takayamachō, Ikoma, Nara 630-0101, Japan', '16564023351656402331633_image.PNG', 'supir', 34.7322411, 135.7342263, '3210101010101088', '2022-06-28 07:39:16', '2022-06-28 07:38:36');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `catatan_order`
--
ALTER TABLE `catatan_order`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `failed_jobs`
--
ALTER TABLE `failed_jobs`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `failed_jobs_uuid_unique` (`uuid`);

--
-- Indexes for table `hasil_tani`
--
ALTER TABLE `hasil_tani`
  ADD PRIMARY KEY (`id_hasil_panen`);

--
-- Indexes for table `kendaraan`
--
ALTER TABLE `kendaraan`
  ADD PRIMARY KEY (`id_kendaraan`);

--
-- Indexes for table `migrations`
--
ALTER TABLE `migrations`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `order_kendaraan`
--
ALTER TABLE `order_kendaraan`
  ADD PRIMARY KEY (`id_order`);

--
-- Indexes for table `password_resets`
--
ALTER TABLE `password_resets`
  ADD KEY `password_resets_email_index` (`email`);

--
-- Indexes for table `pelabuhan`
--
ALTER TABLE `pelabuhan`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `personal_access_tokens`
--
ALTER TABLE `personal_access_tokens`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `supir`
--
ALTER TABLE `supir`
  ADD PRIMARY KEY (`id_supir`);

--
-- Indexes for table `track_lokasi`
--
ALTER TABLE `track_lokasi`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `catatan_order`
--
ALTER TABLE `catatan_order`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `failed_jobs`
--
ALTER TABLE `failed_jobs`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `hasil_tani`
--
ALTER TABLE `hasil_tani`
  MODIFY `id_hasil_panen` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=66;

--
-- AUTO_INCREMENT for table `kendaraan`
--
ALTER TABLE `kendaraan`
  MODIFY `id_kendaraan` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `migrations`
--
ALTER TABLE `migrations`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `order_kendaraan`
--
ALTER TABLE `order_kendaraan`
  MODIFY `id_order` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=81;

--
-- AUTO_INCREMENT for table `pelabuhan`
--
ALTER TABLE `pelabuhan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `personal_access_tokens`
--
ALTER TABLE `personal_access_tokens`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- AUTO_INCREMENT for table `supir`
--
ALTER TABLE `supir`
  MODIFY `id_supir` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `track_lokasi`
--
ALTER TABLE `track_lokasi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=76;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
