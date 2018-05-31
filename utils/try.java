@Async
    public void runCode(Long problemId, String userName, String language, Code code, String dir) {
            System.out.println("333333 "+ userName+ " " + language);
            currentDir = dir;
            Code actual = code;
            Problem actualProblem = getProblem(problemId);
            String codeFolder = getCurrentDir()+actual.getPath();
            Integer index = codeFolder.indexOf("/");
            codeFolder = codeFolder.replace("/"+actual.getName(), "");
            String fileName = actual.getName();
            fileName = language.equalsIgnoreCase("java")? fileName.replace(".java", ""): fileName.replace(".cpp","");
            String compilationName = fileName;
            String outputName = fileName;
            String veredict = "In Queue";
            String bashPath = getCurrentDir();
            System.out.println("Bash Path " + bashPath);
            Integer ac = 0;
            Integer wa = 0;
            List<TestCase> listTC = testCaseService.getTestCasesNameByProblem(problemId);
            String testPath = getCurrentDir()+"/test_cases/"+problemId+"/";
            if( language.equalsIgnoreCase("java")) { 
                bashPath += "/utils/compile_java.sh ";
            } else {
                bashPath += "/utils/compile_c.sh ";
            }
            for( TestCase tc: listTC ) {
                String inputName = testPath+tc.getInputfl();
                String solutionName = testPath+tc.getOutputfl();
                System.out.println("INPUT 3 "+inputName+ " 4: "+ outputName + " 5:" +solutionName+ " 6:" + codeFolder );
                veredict = fileService.runScript(bashPath,fileName+" "+compilationName+" "+inputName+" "+outputName +" "+solutionName+" "+codeFolder+" "+actualProblem.getTimelimit());
                if(veredict.toString().contains("Compilation Error")) {
                    veredict = "Compilation Error";
                    wa = 0;
                    break;
                } else if(veredict.toString().contains("Accepted")){
                    ac++;
                } else if( veredict.toString().contains("timeout")) {
                    veredict = "timeout";
                    wa = 0;
                    break;
                }else if (veredict.toString().contains("runtime") ) {
                    veredict = "runtime";
                    break;
                } else {
                    veredict = "Wrong Answer";
                    wa++;
                    break;
                }
            }
            Submission submission = new Submission();
            Language lang = null;
            if(language.equalsIgnoreCase("java")){
                lang = lang.JAVA;
            } else {
                lang = lang.C;
            }
            Veredict ver = null; 
            if(veredict.toString().contains("Compilation Error")) { 
                ver = ver.COMPILATION_ERROR;
            } else if( veredict.toString().contains("Accepted")){
                ver = ver.ACCEPTED;
            } else if (veredict.toString().contains("timeout") ) {
                ver = ver.TIME_LIMIT;
            } else if (veredict.toString().contains("runtime") ) {
                ver = ver.RUN_TIME_ERROR;
            } else { 
                ver = ver.WRONG_ANSWER;
            }
            submission.setLanguage(lang);
            submission.setProblem(actualProblem);
            submission.setStatus(ver);
            Optional<User> user = userRepository.findOneByLogin(userName);
            if(user.isPresent()) {
                submission.setSubmitter(user.get());
            }
            submission.setDateupload(ZonedDateTime.now());
            submissionRepository.save(submission);
            code.setSubmission(submission);
            codeRepository.save(code);
            
    }
