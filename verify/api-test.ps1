# =====================================================================
#  床位管理模块 —— 5 个接口自测脚本
#
#  用法：
#     1) 先启动后端（任选其一）
#          java -jar "Bed Management\target\bed-management-1.0.0.jar" --spring.profiles.active=h2
#          （或用 MySQL： java -jar "Bed Management\target\bed-management-1.0.0.jar"）
#     2) 再执行本脚本
#          powershell -ExecutionPolicy Bypass -File verify\api-test.ps1
#
#  说明：脚本覆盖「查询 / 修改结束时间 / 床位调换（含各种失败分支）」，
#        调换测试会真的改数据，建议在 h2 内存库或演示库上执行。
# =====================================================================

$ErrorActionPreference = 'Continue'
# 让控制台按 UTF-8 输出中文；重定向到文件时某些环境会设置失败，忽略即可
try { [Console]::OutputEncoding = [System.Text.Encoding]::UTF8 } catch { }

$base      = 'http://localhost:8080/api'
$today     = (Get-Date).ToString('yyyy-MM-dd')
$yesterday = (Get-Date).AddDays(-1).ToString('yyyy-MM-dd')
$pass = 0
$fail = 0

function Invoke-Api {
    param([string]$Method, [string]$Path, $Body, [int]$Timeout = 15)
    $opts = @{ Uri = "$base$Path"; Method = $Method; UseBasicParsing = $true; TimeoutSec = $Timeout }
    if ($null -ne $Body) {
        $opts.Body = ($Body | ConvertTo-Json -Compress)
        $opts.ContentType = 'application/json; charset=utf-8'
    }
    try {
        $resp = Invoke-WebRequest @opts
        $text = [System.Text.Encoding]::UTF8.GetString($resp.RawContentStream.ToArray())
        return [pscustomobject]@{ Status = [int]$resp.StatusCode; Text = $text }
    } catch {
        $r = $_.Exception.Response
        if ($null -eq $r) {
            return [pscustomobject]@{ Status = 0; Text = $_.Exception.Message }
        }
        $reader = New-Object System.IO.StreamReader($r.GetResponseStream(), [System.Text.Encoding]::UTF8)
        $text = $reader.ReadToEnd()
        $reader.Close()
        return [pscustomobject]@{ Status = [int]$r.StatusCode; Text = $text }
    }
}

function Check {
    param([string]$Name, [string]$Method, [string]$Path, $Body, [int]$ExpectStatus, [string]$ExpectText)
    $r = Invoke-Api -Method $Method -Path $Path -Body $Body
    # 期望文本统一转义成字面量，否则 "data":[] 里的方括号会被 -like 当成通配符字符集
    $pattern = '*{0}*' -f [System.Management.Automation.WildcardPattern]::Escape($ExpectText)
    $ok = ($r.Status -eq $ExpectStatus) -and ($r.Text -like $pattern)
    if ($ok) { $script:pass++ } else { $script:fail++ }

    $tag = '[FAIL]'
    if ($ok) { $tag = '[PASS]' }

    $bodyText = ''
    if ($null -ne $Body) { $bodyText = ($Body | ConvertTo-Json -Compress) }
    Write-Output ("---- {0} {1}" -f $tag, $Name)
    Write-Output ("     {0} {1} {2}" -f $Method, $Path, $bodyText)
    Write-Output ("     HTTP {0}（期望 {1}，内容需包含：{2}）" -f $r.Status, $ExpectStatus, $ExpectText)
    Write-Output ("     {0}" -f $r.Text)
    Write-Output ''
}

Write-Output ("======== 床位管理接口自测   今天 = {0} ========" -f $today)
Write-Output ''

# 中文参数统一做 URL 编码，避免手写百分号编码出错
$zhangwei  = [System.Uri]::EscapeDataString('张伟')
$chenguoqiang = [System.Uri]::EscapeDataString('陈国强')
$nobody    = [System.Uri]::EscapeDataString('查无此人')

# -------------------- 1. 床位示意图：床位统计 --------------------
Check '统计：总床位 48 / 空闲 40 / 有人 6 / 外出 2' -Method 'GET' -Path '/bed/statistics' -Body $null `
      -ExpectStatus 200 -ExpectText '"total":48'

Check '统计：空闲 40 张' -Method 'GET' -Path '/bed/statistics' -Body $null `
      -ExpectStatus 200 -ExpectText '"free":40'

Check '统计：有人 6 张' -Method 'GET' -Path '/bed/statistics' -Body $null `
      -ExpectStatus 200 -ExpectText '"occupied":6'

Check '统计：外出 2 张' -Method 'GET' -Path '/bed/statistics' -Body $null `
      -ExpectStatus 200 -ExpectText '"out":2'

# -------------------- 2. 床位示意图：按楼层查房间 + 床位 --------------------
Check '示意图：一层返回 101 房间' -Method 'GET' -Path '/bed/map?floor=1' -Body $null `
      -ExpectStatus 200 -ExpectText '"roomNo":101'

Check '示意图：二层返回 206 房间' -Method 'GET' -Path '/bed/map?floor=2' -Body $null `
      -ExpectStatus 200 -ExpectText '"roomNo":206'

Check '示意图：不存在的楼层返回空数组' -Method 'GET' -Path '/bed/map?floor=9' -Body $null `
      -ExpectStatus 200 -ExpectText '"data":[]'

# -------------------- 3. 床位管理：多条件查询 --------------------
Check '列表：默认查「正在使用」，含张伟的 2 号床' -Method 'GET' -Path '/bed/manage/details' -Body $null `
      -ExpectStatus 200 -ExpectText '606栋-1层-101房间-2号床位'

Check '列表：查使用历史（status=0）' -Method 'GET' -Path '/bed/manage/details?status=0' -Body $null `
      -ExpectStatus 200 -ExpectText '606栋-2层-206房间-1号床位'

Check '列表：显式查「正在使用」（status=1，与页签一致）' -Method 'GET' -Path '/bed/manage/details?status=1' -Body $null `
      -ExpectStatus 200 -ExpectText '606栋-1层-101房间-2号床位'

Check '列表：按客户姓名模糊查询' -Method 'GET' -Path "/bed/manage/details?customerName=$zhangwei" -Body $null `
      -ExpectStatus 200 -ExpectText '张伟'

Check '列表：按入住日期查询' -Method 'GET' -Path '/bed/manage/details?startDate=2025-06-18' -Body $null `
      -ExpectStatus 200 -ExpectText '陈国强'

Check '列表：姓名 + 日期组合查询' -Method 'GET' -Path "/bed/manage/details?customerName=$chenguoqiang&startDate=2025-06-18" -Body $null `
      -ExpectStatus 200 -ExpectText '606栋-2层-202房间-2号床位'

Check '列表：查不到的客户返回空数组' -Method 'GET' -Path "/bed/manage/details?customerName=$nobody" -Body $null `
      -ExpectStatus 200 -ExpectText '"data":[]'

Check '列表：非法的 status 取值按默认（正在使用）处理' -Method 'GET' -Path '/bed/manage/details?status=9' -Body $null `
      -ExpectStatus 200 -ExpectText '张伟'

# -------------------- 4. 修改床位使用结束时间 --------------------
Check '修改结束时间：id=1 改为 2026-03-01（成功）' -Method 'PUT' -Path '/bed/manage/updateEndTime' `
      -Body @{ id = 1; endDate = '2026-03-01' } -ExpectStatus 200 -ExpectText '修改成功'

Check '修改结束时间：结束时间早于入住时间应失败' -Method 'PUT' -Path '/bed/manage/updateEndTime' `
      -Body @{ id = 1; endDate = '2024-01-01' } -ExpectStatus 400 -ExpectText '结束时间不能早于入住时间'

Check '修改结束时间：记录不存在应失败' -Method 'PUT' -Path '/bed/manage/updateEndTime' `
      -Body @{ id = 99999; endDate = '2026-03-01' } -ExpectStatus 400 -ExpectText '床位使用详情不存在'

Check '修改结束时间：id 为空应失败（@NotNull）' -Method 'PUT' -Path '/bed/manage/updateEndTime' `
      -Body @{ endDate = '2026-03-01' } -ExpectStatus 400 -ExpectText '不能为空'

Check '修改结束时间：日期格式非法应失败' -Method 'PUT' -Path '/bed/manage/updateEndTime' `
      -Body @{ id = 1; endDate = 'abc' } -ExpectStatus 400 -ExpectText 'yyyy-MM-dd'

Check '修改结束时间：兼容前端 ISO 日期（2025-01-10T16:00:00.000Z 记为 2025-01-10）' -Method 'PUT' -Path '/bed/manage/updateEndTime' `
      -Body @{ id = 1; endDate = '2025-01-10T16:00:00.000Z' } -ExpectStatus 400 -ExpectText '结束时间不能早于入住时间'

# -------------------- 5. 床位调换 --------------------
Check '调换：日期不是今天应失败' -Method 'POST' -Path '/bed/manage/swap' `
      -Body @{ customerId = 1; newFloor = '1'; newRoomNo = 101; newBedId = 4; swapDate = $yesterday } `
      -ExpectStatus 400 -ExpectText '只能当天调换当天办理'

Check '调换：目标床位已住人应失败' -Method 'POST' -Path '/bed/manage/swap' `
      -Body @{ customerId = 1; newFloor = '1'; newRoomNo = 102; newBedId = 6; swapDate = $today } `
      -ExpectStatus 400 -ExpectText '只能调换到空闲的床位'

Check '调换：床位不属于所选楼层应失败' -Method 'POST' -Path '/bed/manage/swap' `
      -Body @{ customerId = 1; newFloor = '2'; newRoomNo = 101; newBedId = 1; swapDate = $today } `
      -ExpectStatus 400 -ExpectText '不属于所选楼层'

Check '调换：床位不属于所选房间应失败' -Method 'POST' -Path '/bed/manage/swap' `
      -Body @{ customerId = 1; newFloor = '1'; newRoomNo = 102; newBedId = 1; swapDate = $today } `
      -ExpectStatus 400 -ExpectText '不属于所选房间'

Check '调换：客户不存在应失败' -Method 'POST' -Path '/bed/manage/swap' `
      -Body @{ customerId = 99999; newFloor = '1'; newRoomNo = 101; newBedId = 1; swapDate = $today } `
      -ExpectStatus 400 -ExpectText '客户不存在'

Check '调换：目标床位不存在应失败（HTTP 400 + code 400）' -Method 'POST' -Path '/bed/manage/swap' `
      -Body @{ customerId = 1; newFloor = '1'; newRoomNo = 101; newBedId = 99999; swapDate = $today } `
      -ExpectStatus 400 -ExpectText '"code":400,"msg":"目标床位不存在，床位ID：99999"'

Check '调换：张伟 101/2 号床 -> 101/4 号床（成功）' -Method 'POST' -Path '/bed/manage/swap' `
      -Body @{ customerId = 1; newFloor = '1'; newRoomNo = 101; newBedId = 4; swapDate = $today } `
      -ExpectStatus 200 -ExpectText '床位调换成功'

Check '调换后：张伟当前床位变成 101 房间 4 号床' -Method 'GET' -Path "/bed/manage/details?status=1&customerName=$zhangwei" -Body $null `
      -ExpectStatus 200 -ExpectText '606栋-1层-101房间-4号床位'

Check '调换后：原来的 2 号床记录进入使用历史' -Method 'GET' -Path "/bed/manage/details?status=0&customerName=$zhangwei" -Body $null `
      -ExpectStatus 200 -ExpectText '606栋-1层-101房间-2号床位'

Check '调换后：总占用数不变（旧床位释放 + 新床位占用，仍为 6 人）' -Method 'GET' -Path '/bed/statistics' -Body $null `
      -ExpectStatus 200 -ExpectText '"occupied":6'

Check '调换后：一层示意图仍能正常返回' -Method 'GET' -Path '/bed/map?floor=1' -Body $null `
      -ExpectStatus 200 -ExpectText '"roomNo":101'

# -------------------- 汇总 --------------------
Write-Output '======== 测试结果 ========'
Write-Output ("通过 {0} 项，失败 {1} 项" -f $pass, $fail)
if ($fail -gt 0) { exit 1 }
