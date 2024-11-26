Hospital Database Management System (HDMS)
Project Overview
The Hospital Database Management System (HDMS) is designed to streamline and organize hospital operations, providing an efficient way to manage patient records, appointments, prescriptions, billing, and staff details. It uses a relational database backed by SQL to ensure that all hospital data is securely stored and easily accessible.

Key Features
Patient Management: Store and retrieve detailed patient information, including medical history, contact details, and prescriptions.
Appointment Scheduling: Manage appointments, linking patients and doctors while ensuring no time slot conflicts.
Prescription Management: View and update prescriptions, displaying essential details such as medication, dosage, and instructions.
Billing and Payment Tracking: Track and manage billing information, including invoice generation and payment status.
Staff Management: Keep records for hospital staff, including personal details, roles, departments, and schedules.
Reports: Generate summaries on appointments, prescription history, and billing information for administrators.

Technologies Used
Programming Languages:
Java for GUI development (Swing).
SQL for database management.
Database Management System (DBMS): MySQL/PostgreSQL (specify based on your project).
Libraries/Frameworks:
Java Swing for the front-end user interface.
Custom backend logic classes for handling prescriptions and appointments.

Database Structure
Patients Table: Stores patient details such as name, address, medical history, etc.
Appointments Table: Links patients with doctors, recording appointment times and statuses.
Prescriptions Table: Manages medication prescriptions, their descriptions, instructions, and validity periods.
Billing Table: Tracks billing and payment information for patient treatments.
Staff Table: Records details about medical and non-medical staff members.

User Stories
Patient:
View personal prescriptions and appointment history.
Schedule or cancel appointments.
View bills and payment status.
Admin:
Add and manage patient records.
Schedule, update, or cancel patient appointments.
Generate reports on daily appointments, billing, and staff activity.
