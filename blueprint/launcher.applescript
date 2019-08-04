(* INFO
This applescript is used to bootstrap the resulting .app bundle and acts as a launcher.
*)

property extension_list : {"pew"}
property typeIDs_list : {"public.data", "com.example.pew"}

on open droppedItems
	set pathToApp to POSIX path of (path to me)
	repeat with i from 1 to count of droppedItems
		set currentItem to item i of droppedItems
		set itemInfo to info for currentItem
		
		if folder of itemInfo is false then
			try
				set itemExtension to name extension of itemInfo
			on error
				set itemExtension to ""
			end try
			
			try
				set itemType to type identifier of itemInfo
			on error
				set itemType to ""
			end try
			
			if (folder of the itemInfo is false) and ((itemExtension is in the extension_list) or (itemType is in typeIDs_list)) then
				set filePath to the POSIX path of currentItem
				do shell script pathToApp & "Contents/MacOS/app " & quoted form of filePath & " > /dev/null 2>&1 &"
			end if
		end if
	end repeat
end open

on run
	do shell script (POSIX path of (path to me)) & "Contents/MacOS/app > /dev/null 2>&1 &"
end run
