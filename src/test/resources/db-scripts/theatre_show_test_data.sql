INSERT INTO customer (id, name, email, tier) VALUES
('4b7cd538-1f31-416d-9874-6c543f183d72', 'Eva J. Edwards', 'EvaJEdwards@rhyta.com', 1),
('a0c8b01b-898f-4ac0-91be-fc57f177ce94', 'Pauline T. Ditto', 'PaulineTDitto@dayrep.com', 2),
('5e9d267a-84cb-4317-bf75-1ce69d425455', 'Nancy R. Crader', 'NancyRCrader@teleworm.us', 3);

INSERT INTO reservation (id, customer_id, total_price, reservation_time) VALUES
('3bb11a0e-28aa-4606-8ed2-a723203775da', '5e9d267a-84cb-4317-bf75-1ce69d425455', 507, '2024-08-01 23:23:30');

INSERT INTO theatre_show (id, name, venue, start_time, duration_in_mins) VALUES
('64bbc971-800b-4403-83fd-ecdf91bc49cd', 'The Lion King', 'Lyceum Theatre', '2024-08-20 19:30:00', 150),
('53b6cc10-cb03-4610-b698-30acfc47073e', 'The Lion King', 'Lyceum Theatre', '2024-08-21 14:30:00', 150),
('470bdb91-0f3f-418e-80ad-eafafbe741e9', 'The Lion King', 'Lyceum Theatre', '2024-08-21 19:30:00', 150),
('31826112-c2a5-4530-9f92-7db3d33e7a12', 'Harry Potter and the Cursed Child', 'Palace Theatre', '2024-08-21 14:00:00', 120),
('09c0e5b7-ac69-447e-a3df-56f1c7bfc28c', 'Harry Potter and the Cursed Child', 'Palace Theatre', '2024-08-22 14:00:00', 120),
('1f79dd0c-0b29-4777-90f4-c86dbf0ba7f8', 'Wicked', 'Apollo Victoria Theatre', '2024-08-21 14:30:00', 165),
('a3913708-2110-4f2a-a850-81cf3c4c5fd9', 'Wicked', 'Apollo Victoria Theatre', '2024-08-21 19:30:00', 165);

INSERT INTO seat (show_id, seat_id, region, status, price, reservation_id) VALUES
('1f79dd0c-0b29-4777-90f4-c86dbf0ba7f8', 'ZF44',  'Rear', 'AVAILABLE', 62.75, null),
('1f79dd0c-0b29-4777-90f4-c86dbf0ba7f8', 'ZF45',  'Rear', 'AVAILABLE', 62.75, null),
('1f79dd0c-0b29-4777-90f4-c86dbf0ba7f8', 'ZF46',  'Rear', 'AVAILABLE', 62.75, null),
('1f79dd0c-0b29-4777-90f4-c86dbf0ba7f8', 'ZE3',  'Side', 'AVAILABLE', 62.75, null),
('1f79dd0c-0b29-4777-90f4-c86dbf0ba7f8', 'ZE4',  'Side', 'AVAILABLE', 82.75, null),
('1f79dd0c-0b29-4777-90f4-c86dbf0ba7f8', 'ZE5',  'Side', 'AVAILABLE', 82.75, null),
('1f79dd0c-0b29-4777-90f4-c86dbf0ba7f8', 'ZE6',  'Side', 'AVAILABLE', 82.75, null),
('1f79dd0c-0b29-4777-90f4-c86dbf0ba7f8', 'F24',  'Front', 'AVAILABLE', 202.75, null),
('1f79dd0c-0b29-4777-90f4-c86dbf0ba7f8', 'F25',  'Front', 'AVAILABLE', 202.75, null),
('1f79dd0c-0b29-4777-90f4-c86dbf0ba7f8', 'F26',  'Front', 'AVAILABLE', 202.75, null);

INSERT INTO seat (show_id, seat_id, region, status, price, reservation_id) VALUES
('a3913708-2110-4f2a-a850-81cf3c4c5fd9', 'ZF44',  'Rear', 'AVAILABLE', 62.75, null),
('a3913708-2110-4f2a-a850-81cf3c4c5fd9', 'ZF45',  'Rear', 'AVAILABLE', 62.75, null),
('a3913708-2110-4f2a-a850-81cf3c4c5fd9', 'ZF46',  'Rear', 'AVAILABLE', 62.75, null),
('a3913708-2110-4f2a-a850-81cf3c4c5fd9', 'ZE3',  'Side', 'AVAILABLE', 62.75, null),
('a3913708-2110-4f2a-a850-81cf3c4c5fd9', 'ZE4',  'Side', 'AVAILABLE', 82.75, null),
('a3913708-2110-4f2a-a850-81cf3c4c5fd9', 'ZE5',  'Side', 'AVAILABLE', 82.75, null),
('a3913708-2110-4f2a-a850-81cf3c4c5fd9', 'ZE6',  'Side', 'AVAILABLE', 82.75, null),
('a3913708-2110-4f2a-a850-81cf3c4c5fd9', 'F24',  'Front', 'AVAILABLE', 202.75, null),
('a3913708-2110-4f2a-a850-81cf3c4c5fd9', 'F25',  'Front', 'AVAILABLE', 202.75, null),
('a3913708-2110-4f2a-a850-81cf3c4c5fd9', 'F26',  'Front', 'AVAILABLE', 202.75, null);

INSERT INTO seat (show_id, seat_id, region, status, price, reservation_id) VALUES
('09c0e5b7-ac69-447e-a3df-56f1c7bfc28c', 'S19',  'Rear', 'AVAILABLE', 203.5, null),
('09c0e5b7-ac69-447e-a3df-56f1c7bfc28c', 'S20',  'Rear', 'AVAILABLE', 203.5, null),
('09c0e5b7-ac69-447e-a3df-56f1c7bfc28c', 'S21',  'Rear', 'AVAILABLE', 203.5, null),
('09c0e5b7-ac69-447e-a3df-56f1c7bfc28c', 'K27',  'Front', 'AVAILABLE', 303.5, null),
('09c0e5b7-ac69-447e-a3df-56f1c7bfc28c', 'K28',  'Front', 'AVAILABLE', 303.5, null),
('09c0e5b7-ac69-447e-a3df-56f1c7bfc28c', 'K29',  'Front', 'AVAILABLE', 303.5, null),
('09c0e5b7-ac69-447e-a3df-56f1c7bfc28c', 'K30',  'Front', 'AVAILABLE', 303.5, null),
('09c0e5b7-ac69-447e-a3df-56f1c7bfc28c', 'N20',  'Side', 'AVAILABLE', 253.5, null),
('09c0e5b7-ac69-447e-a3df-56f1c7bfc28c', 'N21',  'Side', 'AVAILABLE', 253.5, null),
('09c0e5b7-ac69-447e-a3df-56f1c7bfc28c', 'N22',  'Side', 'AVAILABLE', 253.5, null),
('09c0e5b7-ac69-447e-a3df-56f1c7bfc28c', 'N23',  'Side', 'RESERVED', 253.5, '3bb11a0e-28aa-4606-8ed2-a723203775da'),
('09c0e5b7-ac69-447e-a3df-56f1c7bfc28c', 'N24',  'Side', 'RESERVED', 253.5, '3bb11a0e-28aa-4606-8ed2-a723203775da');




