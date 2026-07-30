$s = New-Object Microsoft.PowerShell.Commands.WebRequestSession

# Lấy CSRF token trước (ứng dụng đặt token vào cookie XSRF-TOKEN và trả về JSON)
$csrf = $null
try {
    $c = Invoke-WebRequest -Uri 'http://localhost:8082/auth/csrf' -Method GET -WebSession $s -UseBasicParsing -ErrorAction Stop
    try { $obj = $c.Content | ConvertFrom-Json; $csrf = $obj.token } catch { $csrf = $null }
    if ([string]::IsNullOrEmpty($csrf)) {
        $cookie = $s.Cookies.GetCookies('http://localhost:8082') | Where-Object { $_.Name -eq 'XSRF-TOKEN' }
        if ($cookie) { $csrf = $cookie.Value }
    }
    Write-Output "CSRF_TOKEN:$csrf"
} catch {
    Write-Output "CSRF_FETCH_FAILED:$($_.Exception.Message)"
}

$body = @{ username = "user"; password = "123456" } | ConvertTo-Json
try {
    $headers = @{}
    if ($csrf) { $headers['X-XSRF-TOKEN'] = $csrf }
    $r = Invoke-WebRequest -Uri 'http://localhost:8082/auth/login' -Method POST -ContentType 'application/json' -Body $body -WebSession $s -UseBasicParsing -Headers $headers -ErrorAction Stop
    Write-Output "LOGIN_STATUS:$($r.StatusCode)"
    Write-Output "LOGIN_BODY:$($r.Content)"
} catch {
    Write-Output "LOGIN_FAILED_ERROR:$($_.Exception.Message)"
    if ($_.Exception.Response -ne $null) { $reader=New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream()); Write-Output $reader.ReadToEnd() }
}

try {
    $o = Invoke-WebRequest -Uri 'http://localhost:8082/orders' -Method GET -WebSession $s -UseBasicParsing -ErrorAction Stop
    Write-Output "ORDERS_STATUS:$($o.StatusCode)"
    Write-Output "ORDERS_BODY:$($o.Content)"
} catch {
    if ($_.Exception.Response -ne $null) {
        $resp = $_.Exception.Response
        Write-Output "ORDERS_FAILED_STATUS:$($resp.StatusCode.Value__)"
        $reader = New-Object System.IO.StreamReader($resp.GetResponseStream())
        Write-Output "ORDERS_FAILED_BODY:"
        Write-Output $reader.ReadToEnd()
    } else {
        Write-Output "ORDERS_FAILED_ERROR:$($_.Exception.Message)"
    }
}