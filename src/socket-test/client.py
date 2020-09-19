import socket

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect(('127.0.0.1', 8080))

while True:
    input()
    sock.send(b'ping')
    d = sock.recv(10)
    print(d)

