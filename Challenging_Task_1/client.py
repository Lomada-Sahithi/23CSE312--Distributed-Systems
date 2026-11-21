
import socket

PORT = 6000
HOST = input("Enter DSM Server IP Address: ")


def send_command(command):
    try:
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.settimeout(5)
            s.connect((HOST, PORT))
            s.sendall((command + "\n").encode())
            return s.recv(1024).decode().strip()
    except ConnectionRefusedError:
        return "ERROR: Connection refused. Is the DSM server running?"
    except OSError as e:
        return "ERROR: " + str(e)


while True:
    print("\n===== DSM MENU =====")
    print("1. Create / Write shared variable")
    print("2. Read shared variable")
    print("3. Increment shared variable")
    print("4. Exit")
    choice = input("Enter choice: ").strip()

    if choice == "1":
        name = input("Variable name: ").strip()
        value = input("Value: ").strip()
        print("Server response:", send_command(f"SET {name} {value}"))

    elif choice == "2":
        name = input("Variable name: ").strip()
        print(f"Value of '{name}':", send_command(f"GET {name}"))

    elif choice == "3":
        name = input("Variable name: ").strip()
        current = send_command(f"GET {name}")
        try:
            new_value = int(current) + 1
            print(f"Read value: {current}")
            print(f"Incremented value: {new_value}")
            print("Server response:", send_command(f"SET {name} {new_value}"))
        except ValueError:
            print(current)

    elif choice == "4":
        print("Exiting...")
        break

    else:
        print("Invalid choice.")
