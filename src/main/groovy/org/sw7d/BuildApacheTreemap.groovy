package org.sw7d

import groovy.json.JsonSlurper

class BuildApacheTreemap {

  static groups = ['apache', 'webservices', 'tuscany', 'tiles', 'tapestry', 'redback', 'myfaces', 'qpid','karaf', 'jackrabbit', 'harmony', 'directory','activemq',
          'hadoop', 'manifoldcf', 'servicemix', 'geronimo', 'struts', 'maven', 'log4j', 'tomcat', 'incubator','commons']

  static void main(args) {
      GithubSearchRetriever search = new GithubSearchRetriever(type: 'repositories', query:'@apache+language:java', loadFromFile: true, getAll: true)
      search.results.items.each {
          println it.name+"\t"+getGroup(it.name)+"\t"+it.size+"\t"+it.watchers_count
      }
      groups.each {
          println it+"_\t"+(it==groups[0]?'':groups[0]+"_")
      }


  }

   static int commitCount(String uri) {
       def commits = new JsonSlurper().parseText(new URL(uri[0..-7]).text)
       return commits?.size() ?: 0
   }

   static String getGroup(String name) {
       String result = groups[0]+"_"
       groups.reverse().each {
         if (name.contains(it)) {
             result = it+"_"
         }
       }
       return result
   }

}
