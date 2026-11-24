import socket

HOST = input("Enter Peer A IP Address: ")
PORT = 12346

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

try:
    client.connect((HOST, PORT))

    print("\nConnected to Peer A successfully!")

    print("\nFiles available in the shared folder:")
    print("--------------------------------------")

    data = b""

    while True:
        chunk = client.recv(4096)

        if not chunk:
            break

        data += chunk

    file_list = data.decode()
    print(file_list)
    print("--------------------------------------")
    print("File listing received successfully!")

except ConnectionRefusedError:
    print("Connection failed! Check whether Peer A is running.")

except OSError as e:
    print("Network error:", e)

finally:
    client.close()
    print("\nConnection closed.")
