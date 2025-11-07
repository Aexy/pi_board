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
   DB_CONNECTION=yourDatabaseUrl*
   PORT=8080*
   JWT_KEY=yourHS512JWTkey
   ```

   - `DB_CONNECTION` should be in the format your PostgreSQL expects (host, port, etc.).
   - `PORT` should be the port that will be used to access the board using `https://localhost:PORT`  
   - If using Supabase, check out the [tutorial](https://medium.com/@khan.abdulwasey99/step-by-step-process-on-how-to-connect-a-spring-boot-application-to-supabase-f1791e1d2402) to find your databse url 
   - Make sure your DB user has privileges to create/read/write tables.

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
   
---


### Setup & Run as MCP Server (example uses Claude Desktop on Windows) + Web Interface: 

1. **Follow the steps above until step 3**

2. **Package the Application**

   ```bash
   mvn clean package -DskipTests
   ```
   
3. **Move the `.jar` file**

   - Maven will create a folder called target, which has the `piboard-...SNAPSHOT.jar` file.
   - Create a new folder under `..\Users\youruser` and name it `piboard`.
   - Copy or move the file to `\Users\youruser\piboard`.

   
4. **Run (`Win+R`) %appdata%**
   
   - Locate the Claude folder under `AppData/Roaming` and open the `claude_desktop_config.json` file.
   
5. **Configure the `claude_desktop_config.json` file**
   
   - Example snippet can be found in the repository as `claude_example.json`.
   - **Important**: Make sure to use forward slash in the `config.json` file for the next steps.
   - Change the JAVA_HOME to point to `.../bin/java` in your device.
   - Change the PI_LOCATION to point to the location of your `.jar` file.
   - `USER_NAME` and `USER_PASS` will only be used for debugging, can be set to anything without spaces.
   - Fields in the `.env` section should match the `.env` file created in step 2.
   
6. **Start or Restart Claude Desktop by quitting first (from the taskbar)**

