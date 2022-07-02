# Tocli

A simple command line interface to manage your todo list.

## Features

- [x] Add a task
- [x] Delete a task
- [ ] List the tasks
  - [x] Filter by status (undone or done)
  - [x] Filter by creation date (after or before)
  - [x] Filter by title with a regex
  - [ ] Filter by todo list
  - [ ] Sort by creation date (ascending or descending)
  - [ ] Sort by alphabetical order (ascending or descending)
  - [ ] Sort by status (undone or done)
- [x] Rename a task
- [x] Mark a task as done
- [x] Mark a task as not done
- [x] Export the tasks to a file in JSON format
- [x] Import the tasks from a file in JSON format

## Requirements

- Gradle
- Java

## Usage

1. Clone the project

```bash
git clone https://github.com/ocineh/Tocli.git
```

2. Run the project with gradle

```bash
gradle run --args="-h"
```

## Example of use

### Add a new task

```bash
tocli add "Buy milk" # Add a task to the default todo list
tocli groceries add "Buy milk" # Add a task to the groceries todo list
```

### Delete a task

```bash
tocli delete 0 # Delete the first task in the default todo list
tocli groceries delete 0 # Delete the first task in the groceries todo list
```

### List the tasks

```bash
tocli list # List all the tasks
tocli list --done # List all the done tasks
tocli list --undone # List all the undone tasks
tocli list --added-before "2020-01-01" # List all the tasks added before 2020-01-01
tocli list --added-after "2020-01-01" # List all the tasks added after 2020-01-01
tocli list --title "^Buy.*" # List all the tasks with the title starting with "Buy"
```

### Rename a task

```bash
tocli rename 0 "Buy milk" # Rename the first task in the default todo list
tocli groceries rename 0 "Buy milk" # Rename the first task in the groceries todo list
```

### Mark a task as done or not done

```bash
tocli done 0 # Mark the first task in the default todo list as done
tocli groceries done 0 # Mark the first task in the groceries todo list as done

tocli undone 0 # Mark the first task in the default todo list as undone
tocli groceries undone 0 # Mark the first task in the groceries todo list as undone
```

### Export/Import the tasks to/from a file in JSON format

```bash
tocli export # Export the tasks in JSON format and print it to the console
tocli export --pretty # Export the tasks in JSON format and print it to the console with pretty print 
tocli export -o tasks.json # Export the tasks in JSON format and save it to tasks.json

tocli import tasks.json # Import the tasks from tasks.json
```

## License

Not licensed yet.

[//]: # (TODO: add a license)
