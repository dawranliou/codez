.PHONY: test

test:
	COAST_ENV=test clj -A:test

clean:
	rm -rf target/*

uberjar:
	clj -A:uberjar

repl:
	clj -R:repl bin/repl.clj

assets:
	clj -m coast.assets
	cp -r resources/public target

copy-bin:
	cp -r bin target

build: assets uberjar copy-bin

server:
	clj -m server

db/migrate:
	clj -m coast.migrations migrate

db/rollback:
	clj -m coast.migrations rollback

db/create:
	clj -m coast.db create

db/drop:
	clj -m coast.db drop
