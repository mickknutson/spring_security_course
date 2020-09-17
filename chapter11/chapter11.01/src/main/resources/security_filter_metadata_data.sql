
-- Security Filter Metadata --

-- insert into security_filter_metadata(id,ant_pattern,expression,sort_order) values (110, '/admin/h2/**','permitAll',10);
insert into security_filter_metadata(id,ant_pattern,expression,sort_order) values (115, '/','permitAll',15);
insert into security_filter_metadata(id,ant_pattern,expression,sort_order) values (120, '/login/*','permitAll',20);
insert into security_filter_metadata(id,ant_pattern,expression,sort_order) values (130, '/logout','permitAll',30);
insert into security_filter_metadata(id,ant_pattern,expression,sort_order) values (140, '/signup/*','permitAll',40);
insert into security_filter_metadata(id,ant_pattern,expression,sort_order) values (150, '/errors/**','permitAll',50);
insert into security_filter_metadata(id,ant_pattern,expression,sort_order) values (160, '/admin/**','local and hasRole("ADMIN")',60);
insert into security_filter_metadata(id,ant_pattern,expression,sort_order) values (170, '/events/','hasRole("ADMIN")',70);
-- insert into security_filter_metadata(id,ant_pattern,expression,sort_order) values (170, '/events/**','hasRole("ADMIN")',70);
insert into security_filter_metadata(id,ant_pattern,expression,sort_order) values (180, '/**','hasRole("USER")',80);
-- insert into security_filter_metadata(id,ant_pattern,expression,sort_order) values (180, '/**','hasRole("ADMIN")',80);

-- the end --
