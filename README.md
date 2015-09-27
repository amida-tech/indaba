# Indaba Platform


Indaba is a collaborative content creation and publishing platform. It consists of the following systems:

- Field Manager (a.k.a. Builder) – this system is used by field workers to collaboratively create content based on pre-defined project configuration. 

- Indaba Admin – this system is used by system admins to manage the Indaba Platform. Main functions include: manage user accounts, manage platform resources, create/configure projects, and manage project executions.

- Publisher – this system makes content created on Indaba Platform available to end users. It allows users with proper rights to view and download content and aggregation results. It also offers web widgets that can be incorporated into 3rd-party websites to display content created on Indaba Platform.

- Workflow Engine – content creation collaboration is coordinated by predefined workflows. This system executes the workflows for all projects.

- Data Aggregator – This system periodically performs aggregation computation against raw data created on Indaba Platform. The results are available through the Publisher system.

- Control Panel – This system will make some functions of Indaba Admin available to 3rd-party project admins. This system is currently under development.


## Building procedures

### Original source code

Folder `legacy` contains original source code of Indaba Platform.

`indaba.20150912.tar.gz` is the source code for Indaba's Admin, Control Panel, Field Manager (aka Builder) and Publisher/Agreggation tools.

`ocs_common.20150904.zip` is source code for common libraries used as part of Indaba platform code.

### Build

Prerequisites: Java, Ant

Run build: 

```
sh ./build.sh
```

After successfull build, `dist` folder will have three .war files: FM31.war, ControlPanel.war and Publisher.war