<?php
// AdminPanel/admin/ads_settings.php
// Manage Wortise ad settings
session_start();

// OPTIONAL: basic auth check placeholder (adjust to your app's auth system)
// if (!isset($_SESSION['admin_logged_in'])) { header('Location: login.php'); exit; }

// Database connection (PDO)
// Update these constants to match your environment
$DB_HOST = getenv('DB_HOST') ?: 'localhost';
$DB_NAME = getenv('DB_NAME') ?: 'thestream';
$DB_USER = getenv('DB_USER') ?: 'root';
$DB_PASS = getenv('DB_PASS') ?: '';
$DB_CHARSET = 'utf8mb4';

$pdo = null;
$error = '';
$success = '';

try {
    $dsn = "mysql:host={$DB_HOST};dbname={$DB_NAME};charset={$DB_CHARSET}";
    $pdo = new PDO($dsn, $DB_USER, $DB_PASS, [
        PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
    ]);
} catch (Throwable $e) {
    $error = 'Database connection failed: ' . htmlspecialchars($e->getMessage());
}

// Ensure settings table exists
if ($pdo) {
    $pdo->exec(
        "CREATE TABLE IF NOT EXISTS ad_settings (
            id INT PRIMARY KEY DEFAULT 1,
            ad_network VARCHAR(50) NOT NULL DEFAULT 'wortise',
            wortise_app_id VARCHAR(255) DEFAULT NULL,
            wortise_banner_id VARCHAR(255) DEFAULT NULL,
            wortise_interstitial_id VARCHAR(255) DEFAULT NULL,
            wortise_native_id VARCHAR(255) DEFAULT NULL,
            wortise_app_open_id VARCHAR(255) DEFAULT NULL,
            updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
        )"
    );
    // Ensure single row with id=1 exists
    $stmt = $pdo->query("SELECT COUNT(*) AS c FROM ad_settings WHERE id=1");
    $rowCount = (int)$stmt->fetchColumn();
    if ($rowCount === 0) {
        $pdo->exec("INSERT INTO ad_settings (id) VALUES (1)");
    }
}

// Handle POST save
if ($pdo && $_SERVER['REQUEST_METHOD'] === 'POST') {
    $ad_network = isset($_POST['ad_network']) ? trim($_POST['ad_network']) : 'wortise';
    $app_id = trim($_POST['wortise_app_id'] ?? '');
    $banner_id = trim($_POST['wortise_banner_id'] ?? '');
    $interstitial_id = trim($_POST['wortise_interstitial_id'] ?? '');
    $native_id = trim($_POST['wortise_native_id'] ?? '');
    $app_open_id = trim($_POST['wortise_app_open_id'] ?? '');

    if ($ad_network !== 'wortise') {
        $ad_network = 'wortise'; // Only wortise supported per requirements
    }

    try {
        $sql = "UPDATE ad_settings SET
                    ad_network = :ad_network,
                    wortise_app_id = :app_id,
                    wortise_banner_id = :banner_id,
                    wortise_interstitial_id = :interstitial_id,
                    wortise_native_id = :native_id,
                    wortise_app_open_id = :app_open_id,
                    updated_at = NOW()
                WHERE id = 1";
        $stmt = $pdo->prepare($sql);
        $stmt->execute([
            ':ad_network' => $ad_network,
            ':app_id' => $app_id,
            ':banner_id' => $banner_id,
            ':interstitial_id' => $interstitial_id,
            ':native_id' => $native_id,
            ':app_open_id' => $app_open_id,
        ]);
        $success = 'Ad settings saved successfully.';
    } catch (Throwable $e) {
        $error = 'Failed to save settings: ' . htmlspecialchars($e->getMessage());
    }
}

// Fetch current settings
$settings = [
    'ad_network' => 'wortise',
    'wortise_app_id' => '',
    'wortise_banner_id' => '',
    'wortise_interstitial_id' => '',
    'wortise_native_id' => '',
    'wortise_app_open_id' => ''
];
if ($pdo) {
    $stmt = $pdo->query("SELECT * FROM ad_settings WHERE id=1 LIMIT 1");
    if ($row = $stmt->fetch()) {
        $settings = array_merge($settings, $row);
    }
}

function h($v) { return htmlspecialchars((string)$v, ENT_QUOTES, 'UTF-8'); }
?>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Wortise Ad Settings</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
  <style>
    body { background: #f6f8fa; }
    .card { box-shadow: 0 2px 6px rgba(0,0,0,.05); }
  </style>
</head>
<body>
<div class="container py-4">
  <nav aria-label="breadcrumb">
    <ol class="breadcrumb">
      <li class="breadcrumb-item"><a href="../index.php">Admin Panel</a></li>
      <li class="breadcrumb-item active" aria-current="page">Wortise Ad Settings</li>
    </ol>
  </nav>

  <div class="row justify-content-center">
    <div class="col-lg-8">
      <?php if ($error): ?>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
          <?php echo $error; ?>
          <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
      <?php endif; ?>
      <?php if ($success): ?>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
          <?php echo $success; ?>
          <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
      <?php endif; ?>

      <div class="card">
        <div class="card-header d-flex align-items-center justify-content-between">
          <h5 class="mb-0">Ad Network Configuration</h5>
          <span class="badge bg-primary">Wortise</span>
        </div>
        <div class="card-body">
          <form method="post" class="needs-validation" novalidate>
            <div class="mb-3">
              <label for="ad_network" class="form-label">Ad Network</label>
              <select class="form-select" id="ad_network" name="ad_network" required>
                <option value="wortise" <?php echo ($settings['ad_network']==='wortise' ? 'selected' : ''); ?>>Wortise</option>
              </select>
              <div class="form-text">Only Wortise is supported in this panel.</div>
            </div>

            <div class="row g-3">
              <div class="col-md-6">
                <label for="wortise_app_id" class="form-label">App ID</label>
                <input type="text" class="form-control" id="wortise_app_id" name="wortise_app_id" value="<?php echo h($settings['wortise_app_id']); ?>" placeholder="e.g. 12345abcd" required>
                <div class="invalid-feedback">Please enter Wortise App ID.</div>
              </div>
              <div class="col-md-6">
                <label for="wortise_banner_id" class="form-label">Banner ID</label>
                <input type="text" class="form-control" id="wortise_banner_id" name="wortise_banner_id" value="<?php echo h($settings['wortise_banner_id']); ?>" placeholder="e.g. banner-12345" required>
                <div class="invalid-feedback">Please enter Banner ID.</div>
              </div>
              <div class="col-md-6">
                <label for="wortise_interstitial_id" class="form-label">Interstitial ID</label>
                <input type="text" class="form-control" id="wortise_interstitial_id" name="wortise_interstitial_id" value="<?php echo h($settings['wortise_interstitial_id']); ?>" placeholder="e.g. inter-12345" required>
                <div class="invalid-feedback">Please enter Interstitial ID.</div>
              </div>
              <div class="col-md-6">
                <label for="wortise_native_id" class="form-label">Native ID</label>
                <input type="text" class="form-control" id="wortise_native_id" name="wortise_native_id" value="<?php echo h($settings['wortise_native_id']); ?>" placeholder="e.g. native-12345" required>
                <div class="invalid-feedback">Please enter Native ID.</div>
              </div>
              <div class="col-md-6">
                <label for="wortise_app_open_id" class="form-label">App Open ID</label>
                <input type="text" class="form-control" id="wortise_app_open_id" name="wortise_app_open_id" value="<?php echo h($settings['wortise_app_open_id']); ?>" placeholder="e.g. appopen-12345" required>
                <div class="invalid-feedback">Please enter App Open ID.</div>
              </div>
            </div>

            <div class="d-flex gap-2 mt-4">
              <button type="submit" class="btn btn-primary">Save Settings</button>
              <a href="./" class="btn btn-outline-secondary">Cancel</a>
            </div>
          </form>
        </div>
        <div class="card-footer text-muted">
          Last updated: <?php echo h($settings['updated_at'] ?? '—'); ?>
        </div>
      </div>

      <div class="mt-4 small text-muted">
        <strong>Note:</strong> Configure your database credentials via environment variables DB_HOST, DB_NAME, DB_USER, DB_PASS or hardcode above.
      </div>
    </div>
  </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<script>
// Bootstrap client-side validation
(() => {
  'use strict';
  const forms = document.querySelectorAll('.needs-validation');
  Array.from(forms).forEach(form => {
    form.addEventListener('submit', event => {
      if (!form.checkValidity()) {
        event.preventDefault();
        event.stopPropagation();
      }
      form.classList.add('was-validated');
    }, false);
  });
})();
</script>
</body>
</html>
