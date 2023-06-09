function turnOffScreen {
    Add-Type -TypeDefinition '
    using System;
    using System.Runtime.InteropServices;
 
    namespace Utilities {
       public static class Display
       {
          [DllImport("user32.dll", CharSet = CharSet.Auto)]
          private static extern IntPtr SendMessage(
             IntPtr hWnd,
             UInt32 Msg,
             IntPtr wParam,
             IntPtr lParam
          );
       }
    }
    '

    [Utilities.Display]::PowerOff()
}
