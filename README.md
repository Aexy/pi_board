# $\pi$ (pi)_board

$\pi$ _board is an open-source kanban style to-do list for everyone. It accurately displays the progress of an issue through states, allows users to list all of their issues and filter and sort them according to their deadlines and gives everyone a chance to plan not only their days, but their months, years and lives.
Because of the uncomplicated user interface, switching to a $\pi$ _board is as easy as putting the pen down. Additionally, it is open-source and free to use, meaning no adds, no premium-plans and no down-payment is needed to boost your planning.

## Get Started
To set up your own $\pi$ _board: 
- Download the latest open JDK (minimum requirement JDK 21). [link](https://openjdk.org/projects/jdk)
- Download [Postgres](https://www.postgresql.org/download).
- Set up a Postgres server and optionally create another user to be used by this application. I suggest using [Supabase](https://supabase.com)
- Write down the username, password and the database url to be used later
  - If Supabase is being used, check out the [tutorial](https://medium.com/@khan.abdulwasey99/step-by-step-process-on-how-to-connect-a-spring-boot-application-to-supabase-f1791e1d2402) to find your databse url.
- Clone this project to a folder.
- In the same folder create an .env file and add the following lines:
  - DB_USER=yourPostgresUser
  - DB_PASS=yourPostgresPass
  - DB_CONNECTION=yourDatabaseUrl
  - Replace the words yourPostgresUser, yourPostgresPass and yourDatabaseUrl with your Postgres user, password and port accordingly.
- Run ``` mvn install ``` or use your ide of your choice to load maven dependencies
- Run the application et voilà, $\pi$ _board is ready.
- To start using the app, head to [register](http://localhost:8090/register).
