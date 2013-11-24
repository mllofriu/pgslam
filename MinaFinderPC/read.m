function alldata = load_data(filename)
    fid = fopen(filename,'r');
    s=0;
    data=[];
    alldata=[];
    save "temp.mat" alldata;
    if fid == -1
        disp("Couldn't find file mydata");
    else
        while (~feof(fid))
            line = fgetl(fid);
            [t1,t2,t3,t4,d] = sscanf(line,'%i:%i:%i:%i %f', "C"); #reading time as hh:mm:ss:ms and data as float
            s++;
            t = (t1 * 3600000 + t2 * 60000 + t3 * 1000 + t4);
            data = [data; t, d];
            if (mod(s,10000) == 0)
                #disp(s), disp("  "), disp(t), disp("  "), disp(d), disp("\n");
                disp(s);
                fflush(stdout);
            end
            if (mod(s,50000) == 0)
                load "temp.mat";
                alldata=[alldata; data];
                data=[];
                save "temp.mat" alldata;
                disp("data saved");
                fflush(stdout);
            end
        end
        disp(s);
        load "temp.mat";
        alldata=[alldata; data];
        save "temp.mat" alldata;
        disp("data saved");
        fflush(stdout); 
    end
    fclose(fid);
