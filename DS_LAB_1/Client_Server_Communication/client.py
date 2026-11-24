import socket

HOST = input("Enter Server IP Address: ")
PORT = 12345

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

try:
    client.connect((HOST, PORT))
    print("Connected to Server Successfully!")

    while True:
        message = input("Client: ")
        client.send(message.encode())

        if message.lower() == "exit":
            break

        reply = client.recv(1024).decode()
        print("Server:", reply)

        if reply.lower() == "exit":
            break

except ConnectionRefusedError:
    print("Connection failed! Check the server.")

except OSError as e:
    print("Network error:", e)

finally:
    client.close()
    print("Client disconnected.")
