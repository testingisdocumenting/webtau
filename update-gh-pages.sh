#!/usr/bin/env bash

set -eux

GIT_URL=$(git config --get remote.origin.url)
GH_PAGES_BRANCH=gh-pages

ROOT_DIR=$(dirname $0)
GH_PAGES_DIR=$ROOT_DIR/gh-pages

# Find the webtau version
WEBTAU_VERSION=$(grep \<version\> $ROOT_DIR/pom.xml | head -1 | cut -d'>' -f2 | cut -d'<' -f1)

# Cleanup on exit
cleanup() {
  rm -rf $GH_PAGES_DIR
}
trap cleanup EXIT

git clone $GIT_URL --branch $GH_PAGES_BRANCH --single-branch --depth 1 $GH_PAGES_DIR

# Cleanup existing pages
rm -rf $GH_PAGES_DIR/guide/*
rm -rf $GH_PAGES_DIR/znaisrc/*

# Copy in new pages
cp -r $ROOT_DIR/webtau-docs/target/guide/* $GH_PAGES_DIR/guide/
cp -r $ROOT_DIR/webtau-docs/target/znaisrc/* $GH_PAGES_DIR/znaisrc/

cd $GH_PAGES_DIR

# Tell git about changed, new and deleted pages
git add -A

git commit -m "Updating docs for $WEBTAU_VERSION"
git push

