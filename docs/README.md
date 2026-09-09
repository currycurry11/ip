# Bo User Guide

Bo supports postponing deadline tasks with the `snooze` and `reschedule`
commands. Only deadline tasks can use these commands.

## Adding deadlines

```text
deadline <description> /by <yyyy-MM-dd>
```

Example: `deadline submit report /by 2026-10-10`

## Snoozing a deadline

```text
snooze <task number> <number of days>d
```

Example: `snooze 2 3d`

This extends task 2 by three days. Repeated snoozing is allowed.

## Rescheduling a deadline

```text
reschedule <task number> <yyyy-MM-dd>
```

Example: `reschedule 2 2026-11-05`

This replaces task 2's deadline with the specified date. Dates before today
are rejected, while completed deadlines remain completed.

The updated date is used by `list`, `upcoming`, and `due`. Only the new date
is stored; the previous date is not retained.
