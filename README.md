# Multitenancy architecture with discriminator column
*Using Java, Spring Web, Spring Data JPA (Hibernate), Spring Security, Bcrypt and JWT.*

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

I wrote an article explaining the main points of the project and how to test: https://link.medium.com/gfmGZTsC43

### Prerequisites
- Java SE Development Kit 8
- PostgresSQL 9+
- Some development IDE that allows you to use the ``Lombok`` plugin. I recommend the excellent ``IntelliJ IDEA``.
- For testing APIs I recommend using ``Postman``.

### Running
The easiest way to run PostgreSQL is using ``Docker``. 
With it installed, just run the command ``docker run --name postgres -p 5432:5432 -d postgres``

Now just run the ``DemoApplication class``. In the ``IntelliJ IDEA`` right click on it and click on ``Run 'DemoApplication.main()'``.