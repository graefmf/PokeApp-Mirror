#!/bin/env python3
import requests
from argparse import ArgumentParser
from uuid import UUID
import json

class Client:
    def __init__(self, uuid, ssl, host):
        self.uuid = None
        self._set_uuid(uuid)
        self.ssl = ssl
        self.host = host
        self.name = None
        self.friends = []

    def _set_uuid(self, uuid):
        if uuid:
            UUID(uuid) # will throw error if invalid
            self.uuid = uuid

    def _get_base_url(self):
        prot = 'https' if self.ssl else 'http'
        s = '{}://{}/'.format(prot, self.host)
        return s

    def register(self, name):
        r = requests.post('{}poke/register'.format(self._get_base_url()),
                data={'name': name})
        c = r.content
        self._set_uuid(str(c, 'utf-8'))

    def add_friend(self, target_uuid: UUID):
        r = requests.post('{}poke/friends/add'.format(self._get_base_url()),
                data={'user': str(self.uuid), 'target': str(target_uuid)})
        c = r.content
        if c == b'success':
            self.friends.append(target_uuid)
        else:
            print('add failed')

    def print_friends(self):
        if len(self.friends):
            for fr in self.friends:
                print(fr)
        else:
            print('No friends')

    def poll(self):
        r = requests.post('{}poke/update'.format(self._get_base_url()),
                data={'user': str(self.uuid)})
        return r.content

    def poke(self, target_uuid):
        pass

if __name__ == '__main__':
    parser = ArgumentParser()
    parser.add_argument('--host', default='localhost:8000', type=str, help='host address')
    parser.add_argument('--ssl', action='store_true', help='use https')
    parser.add_argument('--data', default=None, type=str, help='data file to use')
    parser.add_argument('uuid', default=None, type=str, nargs='?', help='client uuid')
    args = parser.parse_args()
    client = Client(args.uuid, args.ssl, args.host)
    if args.data:
        with open(args.data, 'r') as f:
            data = json.loads(f.read())
        client._set_uuid(data['uuid'])
        client.friends = [UUID(friend) for friend in data['friends']]

    print('h for help')
    while True:
        if not client.uuid: print('WARNING: no uuid set')
        uin = input('> ')
        if uin == 'S':
            try:
                client._set_uuid(input('Enter UUID> '))
            except:
                print('Invalid UUID: set failed')
                continue
        elif uin == 's':
            print('UUID: {}'.format(client.uuid))
        elif uin == 'n':
            print(client.name)
        elif uin == 'r':
            name = input('Enter name> ')
            try:
                client.register(name)
            except:
                print('Register failed')
        elif uin == 'P':
            update_dat = json.loads(client.poll())
            client.friends = [UUID(friend_uuid) for friend_uuid in update_dat['friends']]
            client.name = update_dat['name']
            print(update_dat)
        elif uin == 'p':
            print('no implement')
        elif uin == 'f':
            client.print_friends()
        elif uin == 'a':
            try:
                target_uuid = UUID(input('Enter target UUID> '))
                client.add_friend(target_uuid)
            except:
                print('Invalid UUID')
        elif uin == 'd':
            print('no implement')
        elif uin == 'e':
            with open(input('Path> '), 'w') as f:
                f.write(json.dumps({'uuid': str(client.uuid), 'friends': [str(u) for u in client.friends]}))
        elif uin == 'h':
            print('S: Set UUID\ns: Show UUID\nn: Show name\nr: Register\nP: Poll+Update\np: Poke\nf: List friends\na: Add friend\nd: Remove friend\ne: Export data\nh: Help\nq: Quit')
        elif uin == 'q':
            break
        else:
            print('Invalid option')

