import psutil
import socket

addrs = psutil.net_if_addrs()
stats = psutil.net_if_stats()

for iface, addr_list in addrs.items():
    # Controlla se l'interfaccia è "up" (collegata)
    if iface in stats and stats[iface].isup:
        for addr in addr_list:
            if addr.family == socket.AF_INET:  # IPv4
                print(f"Interfaccia connessa: {iface}")
                print("  IP:", addr.address)
