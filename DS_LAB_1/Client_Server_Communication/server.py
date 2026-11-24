import socket

HOST = '0.0.0.0'
PORT = 12345

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind((HOST, PORT))
server.listen(1)

print("Server is waiting for a connection...")
conn, addr = server.accept()

print("Connected to client:", addr)

while True:
    message = conn.recv(1024).decode()

    if not message or message.lower() == "exit":
        print("Client disconnected.")
        break

    print("Client:", message)

    reply = input("Server: ")
    conn.send(reply.encode())

    if reply.lower() == "exit":
        break

conn.close()
server.close()
print("Server closed.")
