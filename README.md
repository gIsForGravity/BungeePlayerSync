# BungeePlayerSync

## Abstract

In the Minecraft server administration community, a tool called "BungeeCord" is used to link modded "Spigot" servers together. This tool allows server administrators to make large networks out of many smaller servers. Yet this is not very widely used for regular survival experiences in Minecraft. The most common setup is to have multiple survival servers (Survival 1, Survival 2) that are completely seperate but can be switched between to avoid having too many players on one server (if you have too many players, the server gets crowded and performance drops). If something happens on one survival server, it will not happen on the others.

BungeePlayerSync is a new approach to this problem.  The issue of too many players on one survival server is completely solved. The approach of having multiple servers is kept, with a wilderness area that is kept individually on each server. Instead of having this globally, there are town areas, where players and blocks are synced between servers, allowing players to interact with players on other servers if they're in the same town.

## License

This repository uses the [MIT License](LICENSE).
