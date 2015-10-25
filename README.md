# Liferay Patching Tool Info Portlet

**liferay-patching-tool-info-portlet**

The Liferay Patching Tool Info portlet captures details from the Liferay patching-tool and displays those details in the portlet view.


## Overview

The Liferay Patching Tool Info portlet captures details from the Liferay patching-tool and displays those details in the portlet view.

For example,

    % patching-tool info


These details can be useful to portal administrators when reviewing configuration and/or investigating portal issues.


## Supported Products

### GitHub Project Master

* Liferay Portal 6.1 CE : 6.1 CE GA2, GA3 (6.1.1+)
* Liferay Portal 6.1 CE : 6.1 EE GA2, GA3 (6.1.20+)

*NOTE: master reflects branch 6.1.x*

### GitHub Project Branch 6.1.x

* Liferay Portal 6.1 CE : 6.1 CE GA2, GA3 (6.1.1+)
* Liferay Portal 6.1 CE : 6.1 EE GA2, GA3 (6.1.20+)

### GitHub Project Branch 6.2.x

* Liferay Portal 6.2 CE : 6.2 CE GA1 (6.2.0+)
* Liferay Portal 6.2 EE : 6.2 EE GA1 (6.2.10+)


## Downloads

TODO


## Installation

### Liferay Portal + Apache Tomcat Bundle

Deploy "liferay-patching-tool-info-portlet-A.B.C.war" to "LIFERAY_HOME/deploy" folder.


## Usage

### Step 1. Navigate to Liferay Portal Control Panel

### Step 2. Navigate to Patching Tool Info portlet

Select "Patching Tool Info" menu item.

### Step 3. View patching tool info details.

Patching tool info details displayed in portlet view.

### Step 4. Refresh patching tool info details.

Click "Refresh" button.


## Building

### Step 1. Checkout source from GitHub project

Checkout master or branch from GitHub project

eg. Checkout master

    $ git clone https://github.com/permeance/liferay-patching-tool-info-portlet.git
    Cloning into 'liferay-patching-tool-info-portlet'...
    . . .
    $ git status
    On branch master

eg. Checkout branch 6.1.x

    $ git clone https://github.com/permeance/liferay-patching-tool-info-portlet.git
    Cloning into 'liferay-patching-tool-info-portlet'...
    . . .
    $ cd liferay-patching-tool-info-portlet
    $ git checkout -b 6.1.x
    Switched to a new branch '6.1.x'
    $ git status
    On branch 6.1.x
    nothing to commit, working directory clean
    $ git branch
    * 6.1.x
      master

eg. Checkout branch 6.2.x

    $ git clone https://github.com/permeance/liferay-patching-tool-info-portlet.git
    Cloning into 'liferay-patching-tool-info-portlet'...
    . . .
    $ cd liferay-patching-tool-info-portlet
    $ git checkout -b 6.2.x
    Switched to a new branch '6.2.x'
    $ git status
    On branch 6.2.x
    nothing to commit, working directory clean
    $ git branch
    * 6.2.x
      master

### Step 2. Build and package

Build "liferay-patching-tool-info-portlet-A.B.C.war" in the "targets" folder.

    % mvn -U clean package


## Dependencies

Building this project source requires the following tools:

* Oracle JDK 1.7+ 
 * NOTE: Not tested with OpenJDK 1.7+
* Apache Maven 3.2.x+


## Authors

* Terry Mueller <terry.mueller@permeance.com.au>
* Tim Telcik <tim.telcik@permeance.com.au>

