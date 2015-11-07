# Liferay Patching Tool Info Portlet

**liferay-patching-tool-info-portlet**

The Liferay Patching Tool Info portlet display details from the Liferay Portal Enterprise Edition (EE) patching tool.


## Overview

The Liferay Patching Tool Info portlet captures details from the Liferay Portal Enterprise Edition (EE) patching tool and displays those details in the portlet view.

### Sample Command Shell Usage

Similar patching details can be seen by running the patching tool at the command line.

#### Running patching tool in Microsoft Windows command shell

    > cd %LIFERAY_HOME%
    > cd patching-tool
    > patching-tool.bat info

#### Running patching tool in Unix or Linux shell
    
    > cd $LIFERAY_HOME
    % cd patching-tool
    % patching-tool.sh info


The patching details can be useful to portal administrators when reviewing configuration and/or submitting support requests.

*NOTE: The patching tool is only available with Liferay Portal EE. Hence, the Liferay Patching Tool Info portlet is only available for Liferay Portal 6.1.x EE and 6.2.x EE.*


## Supported Products

### Liferay Portal 6.1 EE : 6.1 EE GA2, GA3 (6.1.20+)
* See GitHub Project Master

*NOTE: master reflects branch 6.1.x*

### Liferay Portal 6.1 EE : 6.1 EE GA2, GA3 (6.1.20+)
* GitHub Project Branch 6.1.x

### Liferay Portal 6.2 EE : 6.2 EE GA1 (6.2.10+)
* GitHub Project Branch 6.2.x


## Downloads

* [SourceForge - Liferay Patching Tool Info Portlet](https://sourceforge.net/projects/permeance-apps/files/liferay-patching-tool-info-portlet/ "Liferay Patching Tool Info Portlet")


## Installation

### Liferay Portal + Apache Tomcat Bundle

Deploy "liferay-patching-tool-info-portlet-A.B.C.war" to "LIFERAY_HOME/deploy" folder.


## Usage

### Step 1. Navigate to Liferay Portal Control Panel

#### Liferay Portal 6.1 EE Control Panel Home

![LP 6.1 EE Control Panel Home](/docs/images/liferay-portal/6.1.x/lp-6.1.x-control-panel-info-20151025-annot-800x620.jpg "LP 6.1 EE Control Panel Home")

#### Liferay Portal 6.2 EE Control Panel Home

![LP 6.2 EE Control Panel Home](/docs/images/liferay-portal/6.2.x/lp-6.2.x-control-panel-info-20151025-annot-800x620.jpg "LP 6.2 EE Control Panel Home")


### Step 2. Navigate to Patching Tool Info portlet

Select "Patching Tool Info" menu item.

### Step 3. View patching tool info details.

Patching tool info details displayed in portlet view.

#### Liferay Portal 6.1 EE Control Panel Patching Tool View

![LP 6.1 EE Control Panel Patching Tool View](/docs/images/liferay-portal/6.1.x/lp-6.1.x-control-panel-patching-tool-info-20151025-annot-800x620.jpg "LP 6.1 EE Control Panel Patching Tool View")

#### Liferay Portal 6.2 EE Control Panel Patching Tool View

![LP 6.2 EE Control Panel Patching Tool View](/docs/images/liferay-portal/6.2.x/lp-6.2.x-control-panel-patching-tool-info-20151025-annot-800x620.jpg "LP 6.2 EE Control Panel Patching Tool View")


### Step 4. Refresh patching tool info details.

Click "Refresh" button.


## Building

### Step 1. Checkout source from GitHub project

Checkout master or branch from GitHub project

eg. Checkout master - Liferay Portal 6.1.20+

    $ git clone https://github.com/permeance/liferay-patching-tool-info-portlet.git
    Cloning into 'liferay-patching-tool-info-portlet'...
    . . .
    $ git status
    On branch master

eg. Checkout branch 6.1.x - Liferay Portal 6.1.20+

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

eg. Checkout branch 6.2.x - Liferay Portal 6.2.10+

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


## Troubleshooting

See [Troubleshooting](https://github.com/permeance/liferay-patching-tool-info-portlet/wiki/Troubleshooting) topics in project wiki.


## Dependencies

Building this project source requires the following tools:

* Oracle JDK 1.7+ 
 * NOTE: Not tested with OpenJDK 1.7+
* Apache Maven 3.2.x+


## Authors

* Terry Mueller <terry.mueller@permeance.com.au>
* Tim Telcik <tim.telcik@permeance.com.au>

