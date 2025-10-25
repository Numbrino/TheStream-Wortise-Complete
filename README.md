# TheStream-Wortise-Complete

[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Wortise SDK](https://img.shields.io/badge/Wortise%20SDK-1.7.1-blue.svg)](https://wortise.com)
[![License](https://img.shields.io/badge/License-Private-red.svg)](LICENSE)

## 📋 Overview

This repository contains the **complete TheStream Android application** with full **Wortise Ads SDK 1.7.1** integration, including the admin panel and database setup. This is a production-ready streaming application with monetization capabilities through Wortise advertising network.

### What's Included

- ✅ Complete Android application source code (APK ready)
- ✅ Admin panel for content management
- ✅ Database structure and setup files
- ✅ Wortise Ads SDK 1.7.1 fully integrated
- ✅ Documentation and configuration guides
- ✅ All dependencies and resources

---

## 📂 File Structure

```
TheStream-Wortise-Complete/
│
├── android-app/              # Android application source code
│   ├── app/                  # Main application module
│   │   ├── src/              # Source code
│   │   ├── build.gradle      # App-level Gradle configuration
│   │   └── libs/             # Wortise SDK and dependencies
│   ├── gradle/               # Gradle wrapper files
│   ├── build.gradle          # Project-level Gradle configuration
│   └── settings.gradle       # Project settings
│
├── admin-panel/              # Web-based admin panel
│   ├── index.php             # Main dashboard
│   ├── config.php            # Database configuration
│   ├── assets/               # CSS, JS, images
│   ├── includes/             # PHP includes
│   └── api/                  # API endpoints
│
├── database/                 # Database files
│   ├── thestream.sql         # Database schema and sample data
│   └── migrations/           # Database migration scripts
│
├── docs/                     # Additional documentation
│   ├── wortise-integration.md
│   ├── api-reference.md
│   └── troubleshooting.md
│
└── README.md                 # This file
```

---

## 📥 How to Download and Use

### Method 1: Clone Repository (Recommended)

```bash
git clone https://github.com/Numbrino/TheStream-Wortise-Complete.git
cd TheStream-Wortise-Complete
```

### Method 2: Download ZIP

1. Click the green **Code** button at the top of this page
2. Select **Download ZIP**
3. Extract the downloaded file to your desired location

---

## 📱 Android App Installation

### Prerequisites

- Android Studio Arctic Fox (2020.3.1) or later
- JDK 11 or higher
- Android SDK (API Level 21 minimum, API Level 33+ recommended)
- Gradle 7.0+

### Installation Steps

1. **Open Project in Android Studio**
   ```
   File → Open → Navigate to 'android-app' folder
   ```

2. **Configure Wortise App ID**
   - Open `app/src/main/AndroidManifest.xml`
   - Replace the placeholder Wortise App ID with your actual App ID:
   ```xml
   <meta-data
       android:name="com.wortise.ads.APPLICATION_ID"
       android:value="YOUR_WORTISE_APP_ID" />
   ```

3. **Update Package Name (Optional)**
   - In `build.gradle (app)`, modify the `applicationId` to your unique package name

4. **Sync Gradle**
   - Click **Sync Project with Gradle Files** in the toolbar
   - Wait for dependencies to download

5. **Build APK**
   ```
   Build → Build Bundle(s) / APK(s) → Build APK(s)
   ```

6. **Install on Device**
   - Connect your Android device via USB (enable Developer Mode)
   - Or use an emulator (API Level 30+ recommended)
   - Click the **Run** button in Android Studio

### Generate Signed APK for Production

1. `Build → Generate Signed Bundle / APK`
2. Select **APK** and click **Next**
3. Create or select your keystore
4. Enter keystore credentials
5. Select **release** build variant
6. Click **Finish**

---

## 🌐 Admin Panel Setup

### Prerequisites

- Web server (Apache/Nginx)
- PHP 7.4 or higher
- MySQL 5.7+ or MariaDB 10.3+
- phpMyAdmin (recommended) or MySQL command line

### Installation Steps

1. **Upload Files**
   - Upload the entire `admin-panel/` folder to your web server
   - Recommended path: `/var/www/html/thestream-admin/` or `public_html/admin/`

2. **Set Permissions**
   ```bash
   chmod 755 -R admin-panel/
   chmod 644 admin-panel/config.php
   ```

3. **Configure Database Connection**
   - Open `admin-panel/config.php`
   - Update with your database credentials:
   ```php
   define('DB_HOST', 'localhost');
   define('DB_USER', 'your_username');
   define('DB_PASS', 'your_password');
   define('DB_NAME', 'thestream_db');
   ```

4. **Access Admin Panel**
   - Navigate to: `http://yourdomain.com/admin-panel/`
   - Default login credentials:
     - **Username:** admin
     - **Password:** admin123
   - ⚠️ **IMPORTANT:** Change default password immediately after first login!

5. **Security Recommendations**
   - Enable HTTPS/SSL certificate
   - Change default admin credentials
   - Restrict admin panel access by IP if possible
   - Keep PHP and MySQL updated

---

## 🗄️ Database Setup

### Method 1: Using phpMyAdmin

1. Open phpMyAdmin in your browser
2. Click **New** to create a new database
3. Name it `thestream_db` (or your preferred name)
4. Select **utf8mb4_unicode_ci** collation
5. Click **Import** tab
6. Choose file: `database/thestream.sql`
7. Click **Go** to execute

### Method 2: Using MySQL Command Line

```bash
# Create database
mysql -u root -p -e "CREATE DATABASE thestream_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# Import SQL file
mysql -u root -p thestream_db < database/thestream.sql

# Grant permissions (if using separate user)
mysql -u root -p -e "GRANT ALL PRIVILEGES ON thestream_db.* TO 'your_username'@'localhost'; FLUSH PRIVILEGES;"
```

### Database Configuration in Android App

1. Open `app/src/main/java/com/thestream/config/ApiConfig.java`
2. Update the API base URL:
   ```java
   public static final String BASE_URL = "https://yourdomain.com/admin-panel/api/";
   ```

---

## 🚀 Quick Start Guide

### Step-by-Step for Complete Setup

**Step 1: Database Setup** (15 minutes)
- [ ] Create MySQL database
- [ ] Import `database/thestream.sql`
- [ ] Verify tables created successfully

**Step 2: Admin Panel** (20 minutes)
- [ ] Upload `admin-panel/` to web server
- [ ] Configure `config.php` with database credentials
- [ ] Test admin login
- [ ] Change default password
- [ ] Add sample content (movies/TV shows)

**Step 3: Wortise Account** (10 minutes)
- [ ] Sign up at [Wortise.com](https://wortise.com)
- [ ] Create new Android app in dashboard
- [ ] Note your App ID
- [ ] Configure ad units (Banner, Interstitial, Rewarded)

**Step 4: Android App Configuration** (30 minutes)
- [ ] Open project in Android Studio
- [ ] Update Wortise App ID in `AndroidManifest.xml`
- [ ] Update API base URL in `ApiConfig.java`
- [ ] Change package name (optional but recommended)
- [ ] Sync Gradle and resolve any dependencies

**Step 5: Build & Test** (15 minutes)
- [ ] Build APK in Android Studio
- [ ] Install on test device/emulator
- [ ] Test app launch
- [ ] Verify content loads from admin panel
- [ ] Test ad displays (may show test ads initially)

**Step 6: Production Release** (30 minutes)
- [ ] Generate signed APK with release keystore
- [ ] Test signed APK thoroughly
- [ ] Prepare Play Store listing
- [ ] Upload to Google Play Console
- [ ] Submit for review

**Total Estimated Time:** ~2 hours

---

## ✅ Testing Checklist

### Pre-Release Testing

#### Android App
- [ ] App launches without crashes
- [ ] Splash screen displays correctly
- [ ] Content loads from database via API
- [ ] Video player functions properly
- [ ] Navigation between screens works
- [ ] Search functionality works
- [ ] User registration/login works (if applicable)
- [ ] Favorites/watchlist functions properly

#### Wortise Ads Integration
- [ ] Banner ads load and display
- [ ] Interstitial ads show at appropriate times
- [ ] Rewarded video ads function correctly
- [ ] Ad callbacks handled properly
- [ ] No ad-related crashes
- [ ] Test ads display (before production)
- [ ] Production ads display (after Wortise approval)

#### Admin Panel
- [ ] Login works with correct credentials
- [ ] Dashboard loads all statistics
- [ ] Can add new content (movies/shows)
- [ ] Can edit existing content
- [ ] Can delete content
- [ ] Image uploads work
- [ ] API endpoints return correct data
- [ ] User management functions work
- [ ] Settings save correctly

#### Database
- [ ] All tables created successfully
- [ ] Sample data imported correctly
- [ ] Database connections stable
- [ ] Queries execute efficiently
- [ ] Backups configured

#### Security
- [ ] Admin panel requires authentication
- [ ] Default passwords changed
- [ ] SQL injection protection verified
- [ ] HTTPS enabled (production)
- [ ] API endpoints secured
- [ ] File upload restrictions in place

---

## 🔧 Configuration

### Key Configuration Files

| File | Purpose |
|------|----------|
| `AndroidManifest.xml` | Wortise App ID, permissions |
| `build.gradle` | Dependencies, package name, version |
| `ApiConfig.java` | API endpoints, base URL |
| `config.php` | Database credentials |
| `strings.xml` | App name, texts |

---

## 📞 Support & Troubleshooting

### Common Issues

**Issue:** Ads not showing
- Verify Wortise App ID is correct
- Check if app is approved in Wortise dashboard
- Test ads may take 5-10 minutes to load initially
- Check internet connection

**Issue:** Content not loading
- Verify API base URL is correct
- Check database connection in admin panel
- Ensure database has content
- Check server logs for errors

**Issue:** Build errors in Android Studio
- Clean project: `Build → Clean Project`
- Rebuild: `Build → Rebuild Project`
- Sync Gradle: `File → Sync Project with Gradle Files`
- Check internet connection for dependency downloads

### Additional Resources

- [Wortise Documentation](https://docs.wortise.com/)
- [Android Developer Guide](https://developer.android.com/guide)
- Check `docs/` folder for detailed documentation

---

## 📄 License

This project is private and proprietary. Unauthorized distribution or modification is prohibited.

---

## 🎯 Next Steps

1. ⭐ Star this repository
2. 📖 Read the full documentation in `docs/`
3. 🚀 Follow the Quick Start Guide above
4. 💬 Open an issue if you encounter problems
5. 🔔 Watch this repo for updates

---

## 📊 Version Information

- **App Version:** 1.0.0
- **Wortise SDK:** 1.7.1
- **Min Android Version:** 5.0 (API 21)
- **Target Android Version:** 13 (API 33)
- **Last Updated:** October 2025

---

**Ready to build your streaming empire? Let's get started! 🚀**
