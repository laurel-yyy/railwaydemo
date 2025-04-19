# Railway Ticket Booking System

## System Architecture
- Backend: Spring Boot 3.0.7, Java 17
- Database: MySQL 8.0.33 + ShardingSphere 5.2.1
- Cache/Distributed Lock: Redis

## Quick Start
Make sure Docker and Docker Compose are installed, then run:

```bash
# Build and start
docker-compose up -d

# View logs
docker-compose logs -f

# Access in local
http://localhost/

# Test Account
Please register your own account 

# Notes

# Database tables will be created automatically on first startup
# The system uses ShardingSphere for table sharding
# Redis is used for caching and distributed locks