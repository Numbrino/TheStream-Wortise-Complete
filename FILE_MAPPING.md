# File Mapping Guide

This document shows exactly which file from this repository goes where in your project.

## File Mapping Table

| Source File in Repository | Destination Path in Your Project |
|---------------------------|----------------------------------|
| **Android Application Files** | |
| `Android/app/build.gradle` | `app/build.gradle` |
| `Android/settings.gradle` | `settings.gradle` |
| `Android/app/src/main/java/com/app/thestream/MyApplication.java` | `app/src/main/java/com/[your_package]/MyApplication.java` |
| `Android/app/src/main/java/com/app/thestream/ads/WortiseAppOpenManager.java` | `app/src/main/java/com/[your_package]/ads/WortiseAppOpenManager.java` |
| `Android/app/src/main/java/com/app/thestream/ads/WortiseBannerManager.java` | `app/src/main/java/com/[your_package]/ads/WortiseBannerManager.java` |
| `Android/app/src/main/java/com/app/thestream/ads/WortiseInterstitialManager.java` | `app/src/main/java/com/[your_package]/ads/WortiseInterstitialManager.java` |
| **Additional Ad Manager** | |
| `ads/WortiseNativeManager.java` | `app/src/main/java/com/[your_package]/ads/WortiseNativeManager.java` |
| **Admin Panel Files** | |
| `AdminPanel/admin/` | Copy entire `admin/` folder to your web server |
| `AdminPanel/api/` | Copy entire `api/` folder to your web server |
| **Documentation** | |
| `QUICK_START.md` | Reference guide (not copied to project) |
| `README.md` | Reference guide (not copied to project) |
| `LICENSE` | Include in your project root (if applicable) |

## Important Notes

### 1. Package Name Replacement
- Replace `com/app/thestream` with your actual package name structure
- For example, if your package is `com.mycompany.myapp`, the path would be `com/mycompany/myapp`

### 2. Android Project Integration
- Copy the Java files to your Android project's source directory
- Update the package declarations in each Java file to match your project's package name
- Merge the `build.gradle` dependencies with your existing gradle files

### 3. Admin Panel Deployment
- The `admin/` and `api/` folders should be deployed to your web server
- Configure database connections in the admin panel configuration files
- Set appropriate permissions for web server access

### 4. Ad Manager Setup
- All Wortise ad manager files go into an `ads` package/directory within your app
- Initialize the managers in your Application class (MyApplication.java)
- Make sure to add necessary Wortise SDK dependencies in your build.gradle

## Quick Integration Steps

1. **Copy Android Files**: Transfer all Java files to your Android project with correct package structure
2. **Update Packages**: Change package declarations in Java files to match your project
3. **Merge Gradle**: Add dependencies from `build.gradle` to your existing gradle files
4. **Deploy Admin Panel**: Upload `admin/` and `api/` folders to your web server
5. **Configure**: Update configuration files with your specific settings (API keys, database, etc.)
6. **Test**: Verify all ad placements and admin panel functionality

## File Structure Overview

```
Your Android Project/
├── app/
│   ├── src/main/java/
│   │   └── com/[your_package]/
│   │       ├── MyApplication.java
│   │       └── ads/
│   │           ├── WortiseAppOpenManager.java
│   │           ├── WortiseBannerManager.java
│   │           ├── WortiseInterstitialManager.java
│   │           └── WortiseNativeManager.java
│   └── build.gradle
└── settings.gradle

Your Web Server/
├── admin/
│   └── [admin panel files]
└── api/
    └── [API files]
```

## Support

For detailed setup instructions, refer to `QUICK_START.md` in the repository root.
