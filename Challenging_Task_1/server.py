
import socket
import threading

HOST = '0.0.0.0'
PORT = 6000
memory = {}
lock = threading.Lock()

def handle_client(conn, addr):
    print("Connected to:", addr)
    with conn:
        stream = conn.makefile('rw')
        for line in stream:
            parts = line.strip().split()
            if not parts:
                continue

            command = parts[0].upper()

            with lock:
                if command == "SET" and len(parts) == 3:
                    memory[parts[1]] = parts[2]
                    response = "OK"
                    print(f"[WRITE] {parts[1]} = {parts[2]} (by {addr[0]})")
                elif command == "GET" and len(parts) == 2:
                    if parts[1] in memory:
                        response = memory[parts[1]]
                        print(f"[READ] {parts[1]} = {response} (by {addr[0]})")
                    else:
                        response = "ERROR: Variable not found"
                else:
                    response = "ERROR: Invalid command"

            stream.write(response + "\n")
            stream.flush()
    print("Disconnected:", addr)

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
server.bind((HOST, PORT))
server.listen(5)
print("DSM Server running on port", PORT)

while True:
    conn, addr = server.accept()
    threading.Thread(target=handle_client, args=(conn, addr), daemon=True).start()
