import socket
import os

HOST = '0.0.0.0'
PORT = 12346

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind((HOST, PORT))
server.listen(1)

print("Peer A is waiting for connection...")
conn, addr = server.accept()
print("Connected to Peer B:", addr)

folder_path = input("Enter folder path to share: ")

if os.path.isdir(folder_path):

    files = [
        name for name in os.listdir(folder_path)
        if os.path.isfile(os.path.join(folder_path, name))
    ]

    if files:
        file_list = "\n".join(files)
    else:
        file_list = "No files found in the folder."

else:
    file_list = "ERROR: Invalid folder path."

conn.sendall(file_list.encode())
print("File listing sent successfully.")

conn.close()
server.close()
print("Peer A disconnected.")
