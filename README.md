# π_Board (Pi Board)
Your independent and open-source, personal Kanban & issue tracker

π_Board is a lightweight, open-source board for managing your issues and tasks.  
It provides a clean interface to show the progress of issues through states, list/filter issues by deadlines, and help you plan your workflow — no ads, no premium plans.  

---

## Features

- Kanban-style issue tracking
- Filtering, sorting, and deadline views  
- Simple UI — minimal overhead  
- Fully open-source; self-host friendly  
- Flexible for local or cloud deployments  

---

## Getting Started

### Prerequisites

- Java **17** or newer [(OpenJDK)](https://openjdk.org/projects/jdk)
-  [PostgreSQL](https://www.postgresql.org/download) (or a hosted equivalent, e.g. [Supabase](https://supabase.com))  
- Maven  
- (Optional) Docker / container environment if desired  

---

### Setup & Run Locally

1. **Clone the repo**

   ```bash
   git clone https://github.com/Aexy/pi_board.git
   cd pi_board
   ```

2. **Configure environment variables**

   Create a `.env` file in the project root:

   ```dotenv
   DB_USER=yourPostgresUser
   DB_PASS=yourPostgresPass
   DB_CONNECTION=yourDatabaseUrl
   ```

   - `DB_CONNECTION` should be in the format your PostgreSQL expects (host, port, etc.).  
   - If using Supabase, check out the [tutorial](https://medium.com/@khan.abdulwasey99/step-by-step-process-on-how-to-connect-a-spring-boot-application-to-supabase-f1791e1d2402) to find your databse url 
   - Make sure your DB user has privileges to create/read/write tables, etc.

3. **Install dependencies & build**

   ```bash
   mvn clean install
   ```

4. **Run the application**

   ```bash
   mvn spring-boot:run
   ```

   Or run the packaged JAR:

   ```bash
   java -jar target/pi_board-0.0.1-SNAPSHOT.jar
   ```

5. **Access the application**

   Open your browser and go to the address below to start using your own π_Board

   ```bash
   http://localhost:8080
   ```
