#include <bits/stdc++.h>

using namespace std;

string ltrim(const string &);
string rtrim(const string &);
vector<string> split(const string &);

void printSet(vector<int> s, vector<int> permutationIdx);

/*
 * Complete the 'nonDivisibleSubset' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts following parameters:
 *  1. INTEGER k
 *  2. INTEGER_ARRAY s
 */

bool validateSubset(vector<int> s, int k, vector<int> permutationIdx) {
    //std::cout << "Validating set ";
    printSet(s, permutationIdx);
    if (permutationIdx.size() >= 2)
        for (int i=0; i < permutationIdx.size()-1; i++) {
            int remainder = (s[permutationIdx[i]]+s[permutationIdx[permutationIdx.size()-1]])%k;
            if (remainder == 0) {
                //std::cout << "invalid." << std::endl;
                return false;
            }
        }
    //std::cout << "valid." << std::endl;
    return true;
}

void printSet(vector<int> s, vector<int> permutationIdx) {
    for (int i=0; i<permutationIdx.size(); i++) {
        //std::cout << s[permutationIdx[i]] << " ";
    }
}

int nonDivisibleSubsetRec(int k, vector<int> s, vector<int> permutationIdx, int max) {
    int valid = validateSubset(s, k, permutationIdx);
    max = valid ? permutationIdx.size() : permutationIdx.size()-1;
    if (max == s.size()) return max;

    if (valid) {
        int i = permutationIdx.size() == 0 ? 0 : permutationIdx[permutationIdx.size()-1] + 1;
        for (; i<s.size(); i++) {
            permutationIdx.push_back(i);
            int newMax = nonDivisibleSubsetRec(k, s, permutationIdx, max);
            max = newMax > max ? newMax : max;
            if (max == s.size()) return max;
            permutationIdx.pop_back();
        }
    }

    return max;
}

int nonDivisibleSubset(int k, vector<int> s) {
    int max = 0;
    vector<int> permutationIdx;
    //std::cout << "Starting..." << std::endl;
    max = nonDivisibleSubsetRec(k, s, permutationIdx, max);
    return max;
}

int main()
{
    ofstream fout(getenv("OUTPUT_PATH"));

    string first_multiple_input_temp;
    getline(cin, first_multiple_input_temp);

    vector<string> first_multiple_input = split(rtrim(first_multiple_input_temp));

    int n = stoi(first_multiple_input[0]);

    int k = stoi(first_multiple_input[1]);

    string s_temp_temp;
    getline(cin, s_temp_temp);

    vector<string> s_temp = split(rtrim(s_temp_temp));

    vector<int> s(n);

    for (int i = 0; i < n; i++) {
        int s_item = stoi(s_temp[i]);

        s[i] = s_item;
    }

    int result = nonDivisibleSubset(k, s);

    fout << result << "\n";

    fout.close();

    return 0;
}

string ltrim(const string &str) {
    string s(str);

    s.erase(
        s.begin(),
        find_if(s.begin(), s.end(), not1(ptr_fun<int, int>(isspace)))
    );

    return s;
}

string rtrim(const string &str) {
    string s(str);

    s.erase(
        find_if(s.rbegin(), s.rend(), not1(ptr_fun<int, int>(isspace))).base(),
        s.end()
    );

    return s;
}

vector<string> split(const string &str) {
    vector<string> tokens;

    string::size_type start = 0;
    string::size_type end = 0;

    while ((end = str.find(" ", start)) != string::npos) {
        tokens.push_back(str.substr(start, end - start));

        start = end + 1;
    }

    tokens.push_back(str.substr(start));

    return tokens;
}
