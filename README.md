# todo-cli

A command-line TODO manager for creating, organizing, and tracking tasks. Originally developed as a university assignment in WS 2025/26, then refined for this repository.

## What it can do

- Create tasks with an optional priority and optional deadline
- Group tasks into named lists
- Attach tags to tasks and lists
- Assign tasks as subtasks of other tasks
- Mark tasks as done or open again
- Delete and restore tasks recursively
- Search and filter tasks by list, tag, name, and date range
- Detect duplicate tasks

## Core concepts

### Tasks

Tasks are the central unit of the application. Every task starts in the **open** state and can later be marked as **done**, **deleted**, and restored again. Tasks can also contain other tasks as subtasks, and subtasks can themselves have subtasks.

Each task has:
- a unique numeric ID
- a name
- an optional priority: `HI`, `MD`, or `LO`
- an optional deadline in `YYYY-MM-DD` format
- zero or more tags
- zero or more subtasks

Task names do not have to be unique. A newly created task is not part of any list and is not assigned as a subtask yet. Tasks that are not assigned under another task are top-level tasks.

### Lists

Lists are used to group tasks. A list has a unique name and starts out empty. You can add tasks to a list at any time.

A task can belong to multiple lists. If a task has subtasks, those subtasks are considered part of the list view as well when the parent task is shown there.

### Priorities

Priorities express how urgent or important a task is. The application supports three levels:
- `HI` for high priority
- `MD` for medium priority
- `LO` for low priority

When multiple tasks are shown together, tasks are ordered by priority first (`HI` > `MD` > `LO` > no priority), and then by insertion order.

### Tags

Tags are short labels that help categorize tasks and lists. They make it easier to filter, search, and organize related work.

A task or list can have multiple tags. Tags may contain letters and digits.

### Deadlines

A deadline is the date by which a task should be completed. Deadlines are used for date-based commands such as `upcoming`, `before`, and `between`.

### Display format

Tasks are printed line by line. Open tasks are shown with `[ ]`, completed tasks with `[x]`. Subtasks are displayed underneath their parent task with indentation, so the hierarchy stays visible.

Depending on which properties a task has, output may also include:
- a priority in square brackets, for example `[HI]`
- tags in parentheses, for example `(work, urgent)`
- a deadline after, for example `--> 2026-05-20`

If a task has no tags, the parentheses are omitted. If it has no deadline, the arrow is omitted.

### Input conventions

- `<name>`: a single token without spaces
- `<date>`: `YYYY-MM-DD`
- `<priority>`: `HI`, `MD`, or `LO`
- `<id>`: task ID
- `<tag>`: letters and digits only
- `<list>`: letters only

Optional arguments are shown in square brackets.

## Command reference

### Create and organize

#### `add <name> [priority] [date]`
Create a new top-level task.

#### `add-list <list>`
Create a new empty list.

#### `assign <id> <id>`
Assign a task as a subtask of another task.

#### `assign <id> <list>`
Add a task to a list.

### Tags and metadata

#### `tag <id> <tag>`
Add a tag to a task.

#### `tag <list> <tag>`
Add a tag to a list.

#### `change-date <id> <date>`
Set or replace a task deadline.

#### `change-priority <id> [priority]`
Set, change, or remove a task priority.

### Task state

#### `toggle <id>`
Toggle a task between open and done. This also affects all direct and indirect subtasks.

#### `delete <id>`
Delete a task and all of its subtasks.

#### `restore <id>`
Restore a deleted task and its deleted subtasks.

### Show and filter

#### `show <id>`
Show one task together with all of its subtasks.

#### `todo`
Show all open tasks.

#### `list <list>`
Show all tasks that belong to a list.

#### `tagged-with <tag>`
Show all tasks that use a given tag.

#### `find <name>`
Show all tasks whose name contains the given substring.

#### `upcoming <date>`
Show all tasks due within the 7-day window starting on the given date.

#### `before <date>`
Show all tasks due on or before the given date.

#### `between <date> <date>`
Show all tasks due between two dates, inclusive.

#### `duplicates`
Show all tasks that have at least one duplicate.

### Exit

#### `quit`
Exit the program.


## Example interaction

```text
>add launch_site HI 2026-05-20
added 1: launch_site
>add docs MD 2026-05-14
added 2: docs
>add cli_demo 2026-05-10
added 3: cli_demo
>add tests LO
added 4: tests
>add groceries
added 5: groceries
>add taxes MD 2026-04-15
added 6: taxes
>add release_notes
added 7: release_notes
>add inbox
added 8: inbox
>add inbox HI
added 9: inbox
>add portfolio 2026-04-12
added 10: portfolio
>add-list Work
added Work
>add-list Personal
added Personal
>add-list Home
added Home
>tag 1 urgent
tagged launch_site with urgent
>tag 2 writing
tagged docs with writing
>tag 5 home
tagged groceries with home
>tag 6 finance
tagged taxes with finance
>tag 8 misc
tagged inbox with misc
>tag Work launch
tagged Work with launch
>assign 3 2
assigned cli_demo to docs
>assign 7 2
assigned release_notes to docs
>assign 8 7
assigned inbox to release_notes
>assign 2 1
assigned docs to launch_site
>assign 4 1
assigned tests to launch_site
>assign 1 Work
assigned launch_site to Work
>assign 9 Work
assigned inbox to Work
>assign 6 Personal
assigned taxes to Personal
>assign 10 Personal
assigned portfolio to Personal
>assign 5 Home
assigned groceries to Home
>show 1
- [ ] launch_site [HI]: (urgent) --> 2026-05-20
  - [ ] docs [MD]: (writing) --> 2026-05-14
    - [ ] cli_demo: --> 2026-05-10
    - [ ] release_notes
      - [ ] inbox: (misc)
  - [ ] tests [LO]
>toggle 3
toggled cli_demo and 0 subtasks
>toggle 4
toggled tests and 0 subtasks
>change-date 7 2026-05-11
changed release_notes to 2026-05-11
>change-priority 7 MD
changed release_notes to MD
>toggle 7
toggled release_notes and 1 subtasks
>change-priority 4
changed tests to NONE
>show 1
- [ ] launch_site [HI]: (urgent) --> 2026-05-20
  - [ ] docs [MD]: (writing) --> 2026-05-14
    - [x] release_notes [MD]: --> 2026-05-11
      - [x] inbox: (misc)
    - [x] cli_demo: --> 2026-05-10
  - [x] tests
>list Work
- [ ] launch_site [HI]: (urgent) --> 2026-05-20
  - [ ] docs [MD]: (writing) --> 2026-05-14
    - [x] release_notes [MD]: --> 2026-05-11
      - [x] inbox: (misc)
    - [x] cli_demo: --> 2026-05-10
  - [x] tests
- [ ] inbox [HI]
>tagged-with writing
- [ ] docs [MD]: (writing) --> 2026-05-14
  - [x] release_notes [MD]: --> 2026-05-11
    - [x] inbox: (misc)
  - [x] cli_demo: --> 2026-05-10
>todo
- [ ] launch_site [HI]: (urgent) --> 2026-05-20
  - [ ] docs [MD]: (writing) --> 2026-05-14
- [ ] inbox [HI]
- [ ] taxes [MD]: (finance) --> 2026-04-15
- [ ] groceries: (home)
- [ ] portfolio: --> 2026-04-12
>upcoming 2026-05-08
- [ ] docs [MD]: (writing) --> 2026-05-14
  - [x] release_notes [MD]: --> 2026-05-11
    - [x] inbox: (misc)
  - [x] cli_demo: --> 2026-05-10
>before 2026-05-12
- [ ] taxes [MD]: (finance) --> 2026-04-15
- [x] release_notes [MD]: --> 2026-05-11
  - [x] inbox: (misc)
- [ ] portfolio: --> 2026-04-12
- [x] cli_demo: --> 2026-05-10
>between 2026-04-01 2026-04-30
- [ ] taxes [MD]: (finance) --> 2026-04-15
- [ ] portfolio: --> 2026-04-12
>find in
- [ ] inbox [HI]
- [x] inbox: (misc)
>duplicates
Found 2 duplicates: 8, 9
>delete 2
deleted docs and 3 subtasks
>show 1
- [ ] launch_site [HI]: (urgent) --> 2026-05-20
  - [x] tests
>restore 2
restored docs and 3 subtasks
>show 1
- [ ] launch_site [HI]: (urgent) --> 2026-05-20
  - [ ] docs [MD]: (writing) --> 2026-05-14
    - [x] release_notes [MD]: --> 2026-05-11
      - [x] inbox: (misc)
    - [x] cli_demo: --> 2026-05-10
  - [x] tests
>quit
```
