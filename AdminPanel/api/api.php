<?php
// Set headers for JSON response
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST');
header('Access-Control-Allow-Headers: Content-Type, Authorization');

// Database configuration
require_once '../config/database.php';

// API Key validation
$required_api_key = 'YOUR_SECRET_API_KEY_HERE'; // Change this to your actual API key

// Get API key from request headers or query parameter
$api_key = '';
if (isset($_SERVER['HTTP_X_API_KEY'])) {
    $api_key = $_SERVER['HTTP_X_API_KEY'];
} elseif (isset($_GET['api_key'])) {
    $api_key = $_GET['api_key'];
} elseif (isset($_POST['api_key'])) {
    $api_key = $_POST['api_key'];
}

// Validate API key
if (empty($api_key) || $api_key !== $required_api_key) {
    http_response_code(401);
    echo json_encode([
        'success' => false,
        'message' => 'Invalid or missing API key',
        'error_code' => 'UNAUTHORIZED'
    ]);
    exit;
}

try {
    // Create database connection
    $conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);
    
    // Check connection
    if ($conn->connect_error) {
        throw new Exception('Database connection failed: ' . $conn->connect_error);
    }
    
    // Set charset to utf8mb4
    $conn->set_charset('utf8mb4');
    
    // Query to get all ad configuration from tbl_ads
    $sql = "SELECT * FROM tbl_ads LIMIT 1";
    $result = $conn->query($sql);
    
    if ($result && $result->num_rows > 0) {
        $ad_config = $result->fetch_assoc();
        
        // Prepare response with all ad configuration including Wortise fields
        $response = [
            'success' => true,
            'message' => 'Ad configuration retrieved successfully',
            'data' => [
                // General settings
                'id' => isset($ad_config['id']) ? (int)$ad_config['id'] : null,
                'app_name' => isset($ad_config['app_name']) ? $ad_config['app_name'] : '',
                'package_name' => isset($ad_config['package_name']) ? $ad_config['package_name'] : '',
                
                // Ad network selection
                'ad_network' => isset($ad_config['ad_network']) ? $ad_config['ad_network'] : '',
                'backup_ad_network' => isset($ad_config['backup_ad_network']) ? $ad_config['backup_ad_network'] : '',
                
                // Ad status flags
                'banner_ad_status' => isset($ad_config['banner_ad_status']) ? (int)$ad_config['banner_ad_status'] : 0,
                'interstitial_ad_status' => isset($ad_config['interstitial_ad_status']) ? (int)$ad_config['interstitial_ad_status'] : 0,
                'rewarded_ad_status' => isset($ad_config['rewarded_ad_status']) ? (int)$ad_config['rewarded_ad_status'] : 0,
                'native_ad_status' => isset($ad_config['native_ad_status']) ? (int)$ad_config['native_ad_status'] : 0,
                'app_open_ad_status' => isset($ad_config['app_open_ad_status']) ? (int)$ad_config['app_open_ad_status'] : 0,
                
                // Wortise App ID
                'wortise_app_id' => isset($ad_config['wortise_app_id']) ? $ad_config['wortise_app_id'] : '',
                
                // Wortise Ad Unit IDs
                'wortise_banner_id' => isset($ad_config['wortise_banner_id']) ? $ad_config['wortise_banner_id'] : '',
                'wortise_interstitial_id' => isset($ad_config['wortise_interstitial_id']) ? $ad_config['wortise_interstitial_id'] : '',
                'wortise_rewarded_id' => isset($ad_config['wortise_rewarded_id']) ? $ad_config['wortise_rewarded_id'] : '',
                'wortise_native_id' => isset($ad_config['wortise_native_id']) ? $ad_config['wortise_native_id'] : '',
                'wortise_app_open_id' => isset($ad_config['wortise_app_open_id']) ? $ad_config['wortise_app_open_id'] : '',
                
                // AdMob IDs (if available)
                'admob_app_id' => isset($ad_config['admob_app_id']) ? $ad_config['admob_app_id'] : '',
                'admob_banner_id' => isset($ad_config['admob_banner_id']) ? $ad_config['admob_banner_id'] : '',
                'admob_interstitial_id' => isset($ad_config['admob_interstitial_id']) ? $ad_config['admob_interstitial_id'] : '',
                'admob_rewarded_id' => isset($ad_config['admob_rewarded_id']) ? $ad_config['admob_rewarded_id'] : '',
                'admob_native_id' => isset($ad_config['admob_native_id']) ? $ad_config['admob_native_id'] : '',
                'admob_app_open_id' => isset($ad_config['admob_app_open_id']) ? $ad_config['admob_app_open_id'] : '',
                
                // Facebook Ads IDs (if available)
                'facebook_banner_id' => isset($ad_config['facebook_banner_id']) ? $ad_config['facebook_banner_id'] : '',
                'facebook_interstitial_id' => isset($ad_config['facebook_interstitial_id']) ? $ad_config['facebook_interstitial_id'] : '',
                'facebook_rewarded_id' => isset($ad_config['facebook_rewarded_id']) ? $ad_config['facebook_rewarded_id'] : '',
                'facebook_native_id' => isset($ad_config['facebook_native_id']) ? $ad_config['facebook_native_id'] : '',
                
                // Unity Ads (if available)
                'unity_game_id' => isset($ad_config['unity_game_id']) ? $ad_config['unity_game_id'] : '',
                'unity_banner_id' => isset($ad_config['unity_banner_id']) ? $ad_config['unity_banner_id'] : '',
                'unity_interstitial_id' => isset($ad_config['unity_interstitial_id']) ? $ad_config['unity_interstitial_id'] : '',
                'unity_rewarded_id' => isset($ad_config['unity_rewarded_id']) ? $ad_config['unity_rewarded_id'] : '',
                
                // AppLovin (if available)
                'applovin_banner_id' => isset($ad_config['applovin_banner_id']) ? $ad_config['applovin_banner_id'] : '',
                'applovin_interstitial_id' => isset($ad_config['applovin_interstitial_id']) ? $ad_config['applovin_interstitial_id'] : '',
                'applovin_rewarded_id' => isset($ad_config['applovin_rewarded_id']) ? $ad_config['applovin_rewarded_id'] : '',
                'applovin_native_id' => isset($ad_config['applovin_native_id']) ? $ad_config['applovin_native_id'] : '',
                
                // Click intervals and counters
                'interstitial_ad_click' => isset($ad_config['interstitial_ad_click']) ? (int)$ad_config['interstitial_ad_click'] : 5,
                'rewarded_ad_click' => isset($ad_config['rewarded_ad_click']) ? (int)$ad_config['rewarded_ad_click'] : 1,
                
                // Additional settings
                'is_active' => isset($ad_config['is_active']) ? (int)$ad_config['is_active'] : 1,
                'test_mode' => isset($ad_config['test_mode']) ? (int)$ad_config['test_mode'] : 0,
                'created_at' => isset($ad_config['created_at']) ? $ad_config['created_at'] : null,
                'updated_at' => isset($ad_config['updated_at']) ? $ad_config['updated_at'] : null
            ],
            'timestamp' => time(),
            'version' => '1.0'
        ];
        
        // Return success response with all ad data
        http_response_code(200);
        echo json_encode($response, JSON_PRETTY_PRINT | JSON_UNESCAPED_SLASHES);
        
    } else {
        // No ad configuration found
        http_response_code(404);
        echo json_encode([
            'success' => false,
            'message' => 'No ad configuration found in database',
            'error_code' => 'NOT_FOUND',
            'data' => null
        ], JSON_PRETTY_PRINT);
    }
    
    // Close database connection
    $conn->close();
    
} catch (Exception $e) {
    // Handle errors
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'An error occurred while fetching ad configuration',
        'error' => $e->getMessage(),
        'error_code' => 'SERVER_ERROR'
    ], JSON_PRETTY_PRINT);
}
?>
