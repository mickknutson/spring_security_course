-- ref. Appendix A of Spring Security manual

-- chapter04.01
-- The default USERS schema of Spring Security

-- original password was: user1
UPDATE appUsers SET password = '{bcrypt}$2a$04$DCW33ZKl8Z1xnPJnyD071.NaencHskEx7eWUABVn9/Ks3Mrcq9H5W'
                WHERE email = 'user1@baselogic.com';


-- original password was: admin1
UPDATE appUsers SET password = '{bcrypt}$2a$04$ozZEVR7DTnMSd8EMU.eh6e30oYHyASY49s.oPBf/ig6ltzJsTfHGy'
                WHERE email = 'admin1@baselogic.com';


-- original password was: user2
UPDATE appUsers SET password = '{bcrypt}$2a$04$qfS.WOdonu2VhhUuZspl/.JZllAbNBQoY9fhY8xKWpzIMK8PXI3.O'
                WHERE email = 'user2@baselogic.com';