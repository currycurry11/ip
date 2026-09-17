# Bo User Guide

Bo is a task-management application for creating, organizing, searching, and
completing to-dos, deadlines, and events.

## Quick start

1. Install JDK 25 or later.
2. Run `bo.gui.Launcher` for the graphical interface or `bo.Bo` for the
   command-line interface.
3. Enter a command and press Enter. In the GUI, you can also click **Send**.
4. Enter `bye` to exit.

Bo saves changes automatically in `data/bo.txt`.

## Commands

Use `TASK_NUMBER` for the number shown by `list` and `DATE` in `yyyy-MM-dd`
format.

| Purpose | Command | Example |
| --- | --- | --- |
| Add a to-do | `todo DESCRIPTION` | `todo read chapter 4` |
| Add a deadline | `deadline DESCRIPTION /by DATE` | `deadline submit report /by 2026-10-10` |
| Add an event | `event DESCRIPTION /from START /to END` | `event project meeting /from 2pm /to 4pm` |
| List tasks | `list` | `list` |
| Find tasks | `find KEYWORD` | `find report` |
| Mark complete | `mark TASK_NUMBER` | `mark 2` |
| Mark incomplete | `unmark TASK_NUMBER` | `unmark 2` |
| Delete a task | `delete TASK_NUMBER` | `delete 3` |
| Show upcoming deadlines | `upcoming` | `upcoming` |
| Show deadlines on a date | `due DATE` | `due 2026-10-10` |
| Postpone a deadline | `snooze TASK_NUMBER DAYSd` | `snooze 2 3d` |
| Change a deadline date | `reschedule TASK_NUMBER DATE` | `reschedule 2 2026-11-05` |
| Exit Bo | `bye` | `bye` |

## Notes

- `find` uses case-sensitive partial matching.
- Only deadline tasks can be snoozed or rescheduled.
- Snoozed and rescheduled dates cannot be in the past.
- Completing a deadline does not prevent it from being rescheduled, and its
  completed status is preserved.
- If Bo cannot load the save file, it reports the problem and starts with an
  empty task list. Back up `data/bo.txt` before editing it manually.
