# j2ee.server.home=/path/to/glassfish
GLASSFISH="j2ee.server.home="

# Because... NetBeans
# libs.CopyLibs.classpath=/path/to/org-netbeans-modules-java-j2seproject-copylibstask.jar
NB_COPYLIBSTASK="libs.CopyLibs.classpath="

cd source/common
ant dist \
  -D$GLASSFISH \
  -D$NB_COPYLIBSTASK \
  -q
cd ../..

cd source/builder
ant dist \
  -D$GLASSFISH \
  -D$NB_COPYLIBSTASK \
  -q
cd ../..

cd source/publisher_aggregation
ant dist \
  -D$GLASSFISH \
  -D$NB_COPYLIBSTASK \
  -q
cd ../..

cd source/control_panel
ant dist \
  -D$GLASSFISH \
  -D$NB_COPYLIBSTASK \
  -q
cd ../..

mkdir -p dist

cp source/builder/dist/indaba-builder.war dist/FM31.war
cp source/control_panel/dist/ControlPanel.war dist/ControlPanel.war
cp source/publisher_aggregation/dist/Aggregation.war dist/Publisher.war
