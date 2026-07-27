Add-Type -AssemblyName System.Drawing

$sourcePath = "C:\Users\Ritesh\.gemini\antigravity\brain\ccab27f3-4d56-4e42-bdd2-4e0a78e4ab17\refined_cart_icon_1785173648701.png"
$baseDir = "c:\Users\Ritesh\Music\price checker"

function Resize-Image {
    param(
        [string]$Src,
        [string]$Dest,
        [int]$Size
    )
    $srcImg = [System.Drawing.Image]::FromFile($Src)
    $destImg = New-Object System.Drawing.Bitmap($Size, $Size)
    
    $graphics = [System.Drawing.Graphics]::FromImage($destImg)
    $graphics.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
    $graphics.DrawImage($srcImg, 0, 0, $Size, $Size)
    
    $destImg.Save($Dest, [System.Drawing.Imaging.ImageFormat]::Png)
    $graphics.Dispose()
    $srcImg.Dispose()
    $destImg.Dispose()
}

# Android Mipmaps
$densities = @{
    "mipmap-mdpi" = 48;
    "mipmap-hdpi" = 72;
    "mipmap-xhdpi" = 96;
    "mipmap-xxhdpi" = 144;
    "mipmap-xxxhdpi" = 192
}

foreach ($entry in $densities.GetEnumerator()) {
    $folder = "$baseDir\app\src\main\res\" + $entry.Key
    if (-not (Test-Path $folder)) {
        New-Item -ItemType Directory -Path $folder -Force
    }
    $outFile = "$folder\ic_launcher.png"
    Resize-Image -Src $sourcePath -Dest $outFile -Size $entry.Value
    Write-Host "Created $outFile"
}

# Web Icons
Resize-Image -Src $sourcePath -Dest "$baseDir\docs\icon.png" -Size 512
Resize-Image -Src $sourcePath -Dest "$baseDir\docs\favicon.ico" -Size 64
Resize-Image -Src $sourcePath -Dest "$baseDir\app\src\main\assets\icon.png" -Size 512
Resize-Image -Src $sourcePath -Dest "$baseDir\app\src\main\assets\favicon.ico" -Size 64

Write-Host "Done!"
