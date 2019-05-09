@author Jishun Zhang
@09/May/2019
@perhaps no copyright

to setup server:
java -cp fuckfinal.jar chat.Server [port]

	while running Server accept keyboard input and will shutdown normally when "shutdown" is received;

	the server keeps listening sockets and read from them, store the message in a buffer, 
	the server keeps sending the message to all (even the sender) from buffer if there is a message;

	port is by default 32345;

to use client:
java -cp fuckfinal.jar chat.FuckFinal username host [port]

	username:		plz be identifiable
	host: 		like 192.168.1.1
	port:		by default 32345

	while runing the client accept keyboard input and accept following commands:
	
	send message: send the message with time stamp and signature
	send [-f] filepath(plz encoded in UTF-8): send the file content with *

	read: check if there is new message, if so display on console and also write into ./log.txt for record.


 
