import socket

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.bind(('127.0.0.1', 8080))
sock.listen() 
conn, addr = sock.accept()
while True:
    d = conn.recv(10)
    print(d)
    conn.send(b'pong')
