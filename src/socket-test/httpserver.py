from http.server import HTTPServer, BaseHTTPRequestHandler


class ScanHandler(BaseHTTPRequestHandler):

    def do_GET(self):
        """Serve GET Request"""
        mimetype = 'text/plain'
        self.send_response(200)
        self.send_header('content-type', mimetype)
        self.end_headers()
        self.wfile.write(b'wew lad')


def start_server():
    server = HTTPServer(('0.0.0.0', 8080), ScanHandler)
    try:
        print('Server start')
        server.serve_forever()
    except KeyboardInterrupt:
        print('Server stop')
        server.socket.close()


if __name__ == "__main__":
    start_server()
